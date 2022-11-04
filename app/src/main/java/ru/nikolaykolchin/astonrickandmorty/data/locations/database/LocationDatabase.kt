package ru.nikolaykolchin.astonrickandmorty.data.locations.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nikolaykolchin.astonrickandmorty.data.DatabaseConverters
import ru.nikolaykolchin.astonrickandmorty.data.locations.dto.Location

@Database(entities = [Location::class], version = 1)
@TypeConverters(DatabaseConverters::class)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun getLocationDao(): LocationDao

    companion object {
        private var INSTANCE: LocationDatabase? = null

        suspend fun getInstance(context: Context): LocationDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, LocationDatabase::class.java, "location-database"
                ).build()
                INSTANCE = instance
            }
            return instance
        }
    }
}