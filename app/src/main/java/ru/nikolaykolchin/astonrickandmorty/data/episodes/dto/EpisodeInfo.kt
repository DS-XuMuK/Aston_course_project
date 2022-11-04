package ru.nikolaykolchin.astonrickandmorty.data.episodes.dto

data class EpisodeInfo(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: Any
)