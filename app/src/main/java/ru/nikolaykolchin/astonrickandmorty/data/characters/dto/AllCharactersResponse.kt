package ru.nikolaykolchin.astonrickandmorty.data.characters.dto

data class AllCharactersResponse(
    val info: CharacterInfo,
    val results: List<Character>
)