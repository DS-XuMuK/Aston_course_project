package ru.nikolaykolchin.astonrickandmorty.domain.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_NO_DATA
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_NO_INTERNET
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.characterDao
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.characterService
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.episodeDao
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.episodeService
import ru.nikolaykolchin.astonrickandmorty.data.base.BaseViewModel
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.Character
import ru.nikolaykolchin.astonrickandmorty.data.episodes.dto.Episode
import java.net.UnknownHostException

class EpisodeDetailsViewModel : BaseViewModel() {
    private val _characterLiveData = MutableLiveData<List<Character>>()
    private val _episodeLiveData = MutableLiveData<Episode>()
    private val _statusLiveData = MutableLiveData<String>()
    val characterLiveData: LiveData<List<Character>> get() = _characterLiveData
    val episodeLiveData: LiveData<Episode> get() = _episodeLiveData
    val statusLiveData: LiveData<String> get() = _statusLiveData

    suspend fun loadEpisode(episodeId: Int) {
        try {
            val response = episodeService.getSingleEpisode(episodeId)
            if (response.isSuccessful) {
                response.body()?.let {
                    _episodeLiveData.postValue(it)
                    loadCharactersFromInternet(it)
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            _statusLiveData.postValue(STATUS_NO_INTERNET)
            loadEpisodeFromDatabase(episodeId)
        }
    }

    private suspend fun loadCharactersFromInternet(episode: Episode) {
        val characters = mutableListOf<String>()
        episode.characters.forEach {
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
            loadCharactersFromDatabase(episode)
        }
    }

    private suspend fun loadEpisodeFromDatabase(episodeId: Int) {
        val episode = episodeDao.getEpisodeById(episodeId)
        if (episode == null) {
            _statusLiveData.postValue(STATUS_NO_DATA)
            return
        }
        _episodeLiveData.postValue(episode)
        loadCharactersFromDatabase(episode)
    }

    private suspend fun loadCharactersFromDatabase(episode: Episode) {
        val characterIds = mutableListOf<Int>()
        episode.characters.forEach {
            characterIds.add((it.substringAfterLast('/')).toInt())
        }
        val characterList = characterDao.getCharacterByIdList(characterIds)
        _characterLiveData.postValue(characterList)
    }
}