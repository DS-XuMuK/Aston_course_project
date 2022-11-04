package ru.nikolaykolchin.astonrickandmorty.domain.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_NO_DATA
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_NO_INTERNET
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.characterDao
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.characterService
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.episodeDao
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.episodeService
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.Character
import ru.nikolaykolchin.astonrickandmorty.data.episodes.dto.Episode
import java.net.UnknownHostException

class CharacterDetailsViewModel : ViewModel() {
    private val _characterLiveData = MutableLiveData<Character>()
    private val _episodeLiveData = MutableLiveData<List<Episode>>()
    private val _statusLiveData = MutableLiveData<String>()
    val characterLiveData: LiveData<Character> get() = _characterLiveData
    val episodeLiveData: LiveData<List<Episode>> get() = _episodeLiveData
    val statusLiveData: LiveData<String> get() = _statusLiveData

    suspend fun loadCharacter(characterId: Int) {
        try {
            val response = characterService.getSingleCharacter(characterId)
            if (response.isSuccessful) {
                response.body()?.let {
                    _characterLiveData.postValue(it)
                    loadEpisodesFromInternet(it)
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            _statusLiveData.postValue(STATUS_NO_INTERNET)
            loadCharacterFromDatabase(characterId)
        }
    }

    private suspend fun loadEpisodesFromInternet(character: Character) {
        val episodes = mutableListOf<String>()
        character.episode.forEach {
            episodes.add(it.substringAfterLast('/'))
        }

        try {
            val response = episodeService.getListOfEpisodes(episodes.toString())
            if (response.isSuccessful) {
                response.body()?.let {
                    _episodeLiveData.postValue(it)
                    episodeDao.addList(it as MutableList<Episode>)
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            _statusLiveData.postValue(STATUS_NO_INTERNET)
            loadEpisodesFromDatabase(character)
        }
    }

    private suspend fun loadCharacterFromDatabase(characterId: Int) {
        val character = characterDao.getCharacterById(characterId)
        if (character == null) {
            _statusLiveData.postValue(STATUS_NO_DATA)
            return
        }
        _characterLiveData.postValue(character)
        loadEpisodesFromDatabase(character)
    }

    private suspend fun loadEpisodesFromDatabase(character: Character) {
        val episodeIds = mutableListOf<Int>()
        character.episode.forEach {
            episodeIds.add((it.substringAfterLast('/')).toInt())
        }
        val episodeList = episodeDao.getEpisodeByIdList(episodeIds)
        _episodeLiveData.postValue(episodeList)
    }
}