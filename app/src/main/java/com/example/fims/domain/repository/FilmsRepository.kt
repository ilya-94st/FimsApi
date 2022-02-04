package com.example.fims.domain.repository

import com.example.fims.domain.model.FilmsDitails
import com.example.fims.domain.model.FilmsList
import retrofit2.Response

interface FilmsRepository {

suspend fun getFilms(query: String): Response<FilmsList>

suspend fun getDetailsFilm(id: String): Response<FilmsDitails>
}