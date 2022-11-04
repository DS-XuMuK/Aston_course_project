package ru.nikolaykolchin.astonrickandmorty.data.episodes.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.nikolaykolchin.astonrickandmorty.data.episodes.dto.Episode

@Dao
interface EpisodeDao {
    @Insert(onConflict = REPLACE)
    suspend fun addList(list: MutableList<Episode>?)

    @Query("SELECT COUNT(id) FROM episodes_database")
    fun getItemCount(): Int

    @Query("SELECT * FROM episodes_database")
    suspend fun getAllEpisodes(): List<Episode>

    @Query("SELECT * FROM episodes_database WHERE id=(:id)")
    suspend fun getEpisodeById(id: Int): Episode

    @Query("SELECT * FROM episodes_database WHERE id IN (:idList)")
    suspend fun getEpisodeByIdList(idList: List<Int>): List<Episode>
}