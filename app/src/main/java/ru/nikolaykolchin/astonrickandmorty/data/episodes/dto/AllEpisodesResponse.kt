package ru.nikolaykolchin.astonrickandmorty.data.episodes.dto

data class AllEpisodesResponse(
    val info: EpisodeInfo,
    val results: List<Episode>
)