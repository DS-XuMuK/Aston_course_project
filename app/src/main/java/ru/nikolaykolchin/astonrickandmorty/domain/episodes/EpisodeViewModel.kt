package ru.nikolaykolchin.astonrickandmorty.domain.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_END
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_NO_INTERNET
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.episodeDao
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.episodeService
import ru.nikolaykolchin.astonrickandmorty.data.base.BaseViewModel
import ru.nikolaykolchin.astonrickandmorty.data.episodes.dto.Episode
import java.net.UnknownHostException

class EpisodeViewModel : BaseViewModel() {
    private val listEpisode = mutableListOf<Episode>()
    private val filterList = mutableListOf<String?>(null, null)
    private var page = 1

    private val _listEpisodeLiveData = MutableLiveData(listEpisode)
    private val _loadingStatusLiveData = MutableLiveData("")
    private val _dialogChangeLiveData = MutableLiveData(false)

    val listEpisodeLiveData: LiveData<MutableList<Episode>> get() = _listEpisodeLiveData
    val loadingStatusLiveData: LiveData<String> get() = _loadingStatusLiveData
    val dialogChangeLiveData: LiveData<Boolean> get() = _dialogChangeLiveData

    suspend fun loadData() {
        if (!initialLoadFromDataBase) {
            loadDataFromDatabase()
            initialLoadFromDataBase = true
        }
        try {
            val response = episodeService.getAllEpisodes(page, filterList[0], filterList[1])
            if (response.isSuccessful) {
                response.body()?.let {
                    listEpisode.addAll(it.results)
                    _listEpisodeLiveData.postValue(listEpisode)
                    page++
                    _loadingStatusLiveData.postValue("")
                    episodeDao.addList(listEpisode)
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
        val items = episodeDao.getItemCount()
        if (items == 0) return
        val list = episodeDao.getAllEpisodes()
        var id = 1
        for (episode in list) {
            if (episode.id == id) id++ else break
        }
        id -= (id % 20)

        listEpisode.addAll(list.subList(0, id))
        _listEpisodeLiveData.postValue(listEpisode)
        page = (id / 20) + 1
        _loadingStatusLiveData.postValue("")
    }

    fun receiveDataFromDialog(input: MutableList<String?>) {
        if (filterList != input) {
            filterList.clear()
            filterList.addAll(input)
            listEpisode.clear()
            page = 1
            _dialogChangeLiveData.value = true
            _dialogChangeLiveData.value = false
        }
    }

    companion object {
        var initialLoadFromDataBase = false
    }
}