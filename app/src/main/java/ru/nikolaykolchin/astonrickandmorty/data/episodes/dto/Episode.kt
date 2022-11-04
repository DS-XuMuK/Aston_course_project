package ru.nikolaykolchin.astonrickandmorty.data.episodes.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episodes_database")
data class Episode(
    val air_date: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    @PrimaryKey val id: Int,
    val name: String,
    val url: String
)