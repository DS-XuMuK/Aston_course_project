package ru.nikolaykolchin.astonrickandmorty.data.locations.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.nikolaykolchin.astonrickandmorty.data.locations.dto.Location

@Dao
interface LocationDao {
    @Insert(onConflict = REPLACE)
    suspend fun addList(list: MutableList<Location>?)

    @Query("SELECT COUNT(id) FROM locations_database")
    fun getItemCount(): Int

    @Query("SELECT * FROM locations_database")
    suspend fun getAllLocations(): List<Location>

    @Query("SELECT * FROM locations_database WHERE id=(:id)")
    suspend fun getLocationById(id: Int): Location
}