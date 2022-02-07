package com.example.films.domain.repository

import androidx.paging.PagingData
import com.example.films.domain.model.entities.FilmEntities
import com.example.films.domain.model.response.FilmsDitails
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface FilmsRepository {

 fun getFilms(query: String): Flow<PagingData<FilmEntities>>

suspend fun getDetailsFilm(id: String): Response<FilmsDitails>
}