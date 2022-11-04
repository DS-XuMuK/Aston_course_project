package ru.nikolaykolchin.astonrickandmorty.data.characters.dto

data class CharacterInfo(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: String
)