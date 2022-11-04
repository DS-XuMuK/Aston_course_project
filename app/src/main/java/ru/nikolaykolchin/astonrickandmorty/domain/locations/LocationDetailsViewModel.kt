package ru.nikolaykolchin.astonrickandmorty.domain.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_NO_DATA
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_NO_INTERNET
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.characterDao
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.characterService
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.locationDao
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.locationService
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.Character
import ru.nikolaykolchin.astonrickandmorty.data.locations.dto.Location
import java.net.UnknownHostException

class LocationDetailsViewModel : ViewModel() {
    private val _characterLiveData = MutableLiveData<List<Character>>()
    private val _locationLiveData = MutableLiveData<Location>()
    private val _statusLiveData = MutableLiveData<String>()
    val characterLiveData: LiveData<List<Character>> get() = _characterLiveData
    val locationLiveData: LiveData<Location> get() = _locationLiveData
    val statusLiveData: LiveData<String> get() = _statusLiveData

    suspend fun loadLocation(locationId: Int) {
        try {
            val response = locationService.getSingleLocation(locationId)
            if (response.isSuccessful) {
                response.body()?.let {
                    _locationLiveData.postValue(it)
                    loadCharactersFromInternet(it)
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            _statusLiveData.postValue(STATUS_NO_INTERNET)
            loadLocationFromDatabase(locationId)
        }
    }

    private suspend fun loadCharactersFromInternet(location: Location) {
        val characters = mutableListOf<String>()
        location.residents.forEach {
            characters.add(it.substringAfterLast('/'))
        }

        try {
            val response = characterService.getListOfCharacters(characters.toString())
            if (response.isSuccessful) {
                response.body()?.let {
                    _characterLiveData.postValue(it)
                    characterDao.addList(it as MutableList<Character>)
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            _statusLiveData.postValue(STATUS_NO_INTERNET)
            loadCharactersFromDatabase(location)
        }
    }

    private suspend fun loadLocationFromDatabase(locationId: Int) {
        val location = locationDao.getLocationById(locationId)
        if (location == null) {
            _statusLiveData.postValue(STATUS_NO_DATA)
            return
        }
        _locationLiveData.postValue(location)
        loadCharactersFromDatabase(location)
    }

    private suspend fun loadCharactersFromDatabase(location: Location) {
        val characterIds = mutableListOf<Int>()
        location.residents.forEach {
            characterIds.add((it.substringAfterLast('/')).toInt())
        }
        val characterList = characterDao.getCharacterByIdList(characterIds)
        _characterLiveData.postValue(characterList)
    }
}