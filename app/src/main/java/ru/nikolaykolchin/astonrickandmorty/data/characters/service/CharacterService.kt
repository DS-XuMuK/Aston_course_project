package ru.nikolaykolchin.astonrickandmorty.data.characters.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.AllCharactersResponse
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.Character

interface CharacterService {

    @GET("character/")
    suspend fun getAllCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("gender") gender: String?
    ): Response<AllCharactersResponse>

    @GET("character/{id}")
    suspend fun getSingleCharacter(
        @Path("id") id: Int
    ): Response<Character>

    @GET("character/{list}")
    suspend fun getListOfCharacters(
        @Path("list") list: String
    ): Response<List<Character>>
}