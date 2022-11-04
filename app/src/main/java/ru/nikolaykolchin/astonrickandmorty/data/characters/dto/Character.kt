package ru.nikolaykolchin.astonrickandmorty.data.characters.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters_database")
data class Character(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @Embedded val origin: CharacterOrigin,
    @Embedded val location: CharacterLocation,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)