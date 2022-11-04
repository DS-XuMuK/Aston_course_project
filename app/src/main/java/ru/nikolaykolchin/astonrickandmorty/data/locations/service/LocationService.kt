package ru.nikolaykolchin.astonrickandmorty.data.locations.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.nikolaykolchin.astonrickandmorty.data.locations.dto.AllLocationsResponse
import ru.nikolaykolchin.astonrickandmorty.data.locations.dto.Location

interface LocationService {

    @GET("location/")
    suspend fun getAllLocations(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("type") dimension: String?
    ): Response<AllLocationsResponse>

    @GET("location/{id}")
    suspend fun getSingleLocation(
        @Path("id") id: Int
    ): Response<Location>
}