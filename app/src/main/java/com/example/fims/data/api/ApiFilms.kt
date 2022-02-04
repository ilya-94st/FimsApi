package com.example.fims.data.api

import com.example.fims.common.Constants
import com.example.fims.domain.model.FilmsDitails
import com.example.fims.domain.model.FilmsList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiFilms {

    @GET("/")
    suspend fun getFilms(
        @Query("s")
        query: String,
        @Query("page")
        page: Int = 1,
        @Query("apiKey")
        apiKey: String = Constants.API_KEY
    ): Response<FilmsList>

    @GET("/")
    suspend fun getDetailsFilm(
        @Query("i")
        id: String,
        @Query("apiKey")
        apiKey: String = Constants.API_KEY
    ): Response<FilmsDitails>
}