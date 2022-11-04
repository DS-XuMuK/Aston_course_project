package ru.nikolaykolchin.astonrickandmorty.data.episodes.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.nikolaykolchin.astonrickandmorty.data.episodes.dto.AllEpisodesResponse
import ru.nikolaykolchin.astonrickandmorty.data.episodes.dto.Episode

interface EpisodeService {

    @GET("episode/")
    suspend fun getAllEpisodes(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") episode: String?
    ): Response<AllEpisodesResponse>

    @GET("episode/{id}")
    suspend fun getSingleEpisode(
        @Path("id") id: Int
    ): Response<Episode>

    @GET("episode/{list}")
    suspend fun getListOfEpisodes(
        @Path("list") list: String
    ): Response<List<Episode>>
}