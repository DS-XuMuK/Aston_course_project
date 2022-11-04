package ru.nikolaykolchin.astonrickandmorty.data.characters.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.Character

@Dao
interface CharacterDao {
    @Insert(onConflict = REPLACE)
    suspend fun addList(list: MutableList<Character>?)

    @Query("SELECT COUNT(id) FROM characters_database")
    fun getItemCount(): Int

    @Query("SELECT * FROM characters_database")
    suspend fun getAllCharacters(): List<Character>

    @Query("SELECT * FROM characters_database WHERE id=(:id)")
    suspend fun getCharacterById(id: Int): Character

    @Query("SELECT * FROM characters_database WHERE id IN (:idList)")
    suspend fun getCharacterByIdList(idList: List<Int>): List<Character>

    @Query("SELECT * FROM characters_database WHERE name LIKE :name")
    suspend fun findByName(name: String): List<Character>

    @Query("SELECT * FROM characters_database WHERE status LIKE :status")
    suspend fun findByStatus(status: String): List<Character>

    @Query("SELECT * FROM characters_database WHERE species LIKE :species")
    suspend fun findBySpecies(species: String): List<Character>

    @Query("SELECT * FROM characters_database WHERE type LIKE :type")
    suspend fun findByType(type: String): List<Character>

    @Query("SELECT * FROM characters_database WHERE gender LIKE :gender")
    suspend fun findByGender(gender: String): List<Character>
}