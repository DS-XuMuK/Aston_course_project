package ru.nikolaykolchin.astonrickandmorty.data.episodes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nikolaykolchin.astonrickandmorty.data.DatabaseConverters
import ru.nikolaykolchin.astonrickandmorty.data.episodes.dto.Episode

@Database(entities = [Episode::class], version = 1)
@TypeConverters(DatabaseConverters::class)
abstract class EpisodeDatabase : RoomDatabase() {
    abstract fun getEpisodeDao(): EpisodeDao

    companion object {
        private var INSTANCE: EpisodeDatabase? = null

        suspend fun getInstance(context: Context): EpisodeDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, EpisodeDatabase::class.java, "episode-database"
                ).build()
                INSTANCE = instance
            }
            return instance
        }
    }
}