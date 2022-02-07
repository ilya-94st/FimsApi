package com.example.films.data.api

import com.example.films.common.Constants
import com.example.films.domain.model.response.FilmsDitails
import com.example.films.domain.model.response.FilmsList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ApiFilms {

    @GET("/")
    suspend fun getFilms(
        @Query("s")
        query: String,
        @Query("pageSize")
        pageSize: Int,
        @Query("page")
        page: Int,
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


    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>
}