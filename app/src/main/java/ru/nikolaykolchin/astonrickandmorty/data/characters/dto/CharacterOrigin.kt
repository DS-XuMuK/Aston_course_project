package ru.nikolaykolchin.astonrickandmorty.data.characters.dto

import androidx.room.ColumnInfo

data class CharacterOrigin(
    @ColumnInfo(name = "origin_name") val name: String,
    @ColumnInfo(name = "origin_url") val url: String
)