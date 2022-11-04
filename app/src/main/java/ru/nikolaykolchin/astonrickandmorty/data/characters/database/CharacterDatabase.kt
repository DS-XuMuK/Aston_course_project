package ru.nikolaykolchin.astonrickandmorty.data.characters.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nikolaykolchin.astonrickandmorty.data.DatabaseConverters
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.Character

@Database(entities = [Character::class], version = 1)
@TypeConverters(DatabaseConverters::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao

    companion object {
        private var INSTANCE: CharacterDatabase? = null

        suspend fun getInstance(context: Context): CharacterDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    CharacterDatabase::class.java,
                    "character-database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
            }
            return instance
        }
    }
}