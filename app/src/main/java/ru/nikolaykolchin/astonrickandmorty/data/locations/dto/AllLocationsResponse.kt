package ru.nikolaykolchin.astonrickandmorty.data.locations.dto

data class AllLocationsResponse(
    val info: LocationInfo,
    val results: List<Location>
)