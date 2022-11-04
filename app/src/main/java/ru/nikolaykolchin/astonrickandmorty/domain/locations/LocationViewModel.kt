package ru.nikolaykolchin.astonrickandmorty.domain.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_END
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_NO_INTERNET
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.locationDao
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.locationService
import ru.nikolaykolchin.astonrickandmorty.data.base.BaseViewModel
import ru.nikolaykolchin.astonrickandmorty.data.locations.dto.Location
import java.net.UnknownHostException

class LocationViewModel : BaseViewModel() {
    private val listLocation = mutableListOf<Location>()
    private val filterList = mutableListOf<String?>(null, null, null)
    private var page = 1

    private val _listLocationLiveData = MutableLiveData(listLocation)
    private val _loadingStatusLiveData = MutableLiveData("")
    private val _dialogChangeLiveData = MutableLiveData(false)

    val listLocationLiveData: LiveData<MutableList<Location>> get() = _listLocationLiveData
    val loadingStatusLiveData: LiveData<String> get() = _loadingStatusLiveData
    val dialogChangeLiveData: LiveData<Boolean> get() = _dialogChangeLiveData

    suspend fun loadData() {
        if (!initialLoadFromDataBase) {
            loadDataFromDatabase()
            initialLoadFromDataBase = true
        }
        try {
            val response =
                locationService.getAllLocations(page, filterList[0], filterList[1], filterList[2])
            if (response.isSuccessful) {
                response.body()?.let {
                    listLocation.addAll(it.results)
                    _listLocationLiveData.postValue(listLocation)
                    page++
                    _loadingStatusLiveData.postValue("")
                    locationDao.addList(listLocation)
                }
            } else {
                _loadingStatusLiveData.postValue(STATUS_END)
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            _loadingStatusLiveData.postValue(STATUS_NO_INTERNET)
        }
    }

    private suspend fun loadDataFromDatabase() {
        val items = locationDao.getItemCount()
        if (items == 0) return
        val list = locationDao.getAllLocations()

        listLocation.addAll(list)
        _listLocationLiveData.postValue(listLocation)
        page = (items / 20) + 1
        _loadingStatusLiveData.postValue("")
    }

    fun receiveDataFromDialog(input: MutableList<String?>) {
        if (filterList != input) {
            filterList.clear()
            filterList.addAll(input)
            listLocation.clear()
            page = 1
            _dialogChangeLiveData.value = true
            _dialogChangeLiveData.value = false
        }
    }

    companion object {
        var initialLoadFromDataBase = false
    }
}
