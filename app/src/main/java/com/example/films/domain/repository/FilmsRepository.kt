package com.example.films.domain.repository

import androidx.paging.PagingData
import com.example.films.domain.model.entinity.EntityFilms
import com.example.films.domain.model.response.FilmsDitails
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface FilmsRepository {

 fun getFilms(query: String): Flow<PagingData<EntityFilms>>

suspend fun getDetailsFilm(id: String): Response<FilmsDitails>
}