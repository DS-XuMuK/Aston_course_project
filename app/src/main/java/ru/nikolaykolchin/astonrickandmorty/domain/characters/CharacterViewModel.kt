package ru.nikolaykolchin.astonrickandmorty.domain.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_END
import ru.nikolaykolchin.astonrickandmorty.data.Constants.STATUS_NO_INTERNET
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.characterDao
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.characterService
import ru.nikolaykolchin.astonrickandmorty.data.base.BaseViewModel
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.Character
import java.net.UnknownHostException

class CharacterViewModel : BaseViewModel() {
    private val listCharacter = mutableListOf<Character>()
    private val filterList = mutableListOf<String?>(null, null, null, null, null)
    private var page = 1

    private val _listCharacterLiveData = MutableLiveData(listCharacter)
    private val _loadingStatusLiveData = MutableLiveData("")
    private val _dialogChangeLiveData = MutableLiveData(false)

    val listCharacterLiveData: LiveData<MutableList<Character>> get() = _listCharacterLiveData
    val loadingStatusLiveData: LiveData<String> get() = _loadingStatusLiveData
    val dialogChangeLiveData: LiveData<Boolean> get() = _dialogChangeLiveData

    suspend fun loadData() {
        if (!initialLoadFromDataBase) {
            loadDataFromDatabase()
            initialLoadFromDataBase = true
        }
        try {
            val response = characterService.getAllCharacters(
                page, filterList[0], filterList[1], filterList[2], filterList[3], filterList[4]
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    listCharacter.addAll(it.results)
                    _listCharacterLiveData.postValue(listCharacter)
                    page++
                    _loadingStatusLiveData.postValue("")
                    characterDao.addList(listCharacter)
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
        val items = characterDao.getItemCount()
        if (items == 0) return
        val list = characterDao.getAllCharacters()
        var id = 1
        for (character in list) {
            if (character.id == id) id++ else break
        }
        id -= (id % 20)

        listCharacter.addAll(list.subList(0, id))
        _listCharacterLiveData.postValue(listCharacter)
        page = (id / 20) + 1
        _loadingStatusLiveData.postValue("")
    }

    fun receiveDataFromDialog(input: MutableList<String?>) {
        if (filterList != input) {
            filterList.clear()
            filterList.addAll(input)
            listCharacter.clear()
            page = 1
            _dialogChangeLiveData.value = true
            _dialogChangeLiveData.value = false
        }
    }

    companion object {
        var initialLoadFromDataBase = false
    }
}