package ru.nikolaykolchin.astonrickandmorty.data.characters.dto

import androidx.room.ColumnInfo

data class CharacterLocation(
    @ColumnInfo(name = "location_name") val name: String,
    @ColumnInfo(name = "location_url") val url: String
)