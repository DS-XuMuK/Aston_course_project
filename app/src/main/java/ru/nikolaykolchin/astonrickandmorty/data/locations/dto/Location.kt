package ru.nikolaykolchin.astonrickandmorty.data.locations.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations_database")
data class Location(
    val created: String,
    val dimension: String,
    @PrimaryKey val id: Int,
    val name: String,
    val residents: List<String>,
    val type: String,
    val url: String
)