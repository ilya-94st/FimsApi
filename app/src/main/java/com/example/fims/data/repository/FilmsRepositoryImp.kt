package com.example.fims.data.repository

import com.example.fims.data.api.ApiFilms
import com.example.fims.domain.model.FilmsDitails
import com.example.fims.domain.model.FilmsList
import com.example.fims.domain.repository.FilmsRepository
import retrofit2.Response
import javax.inject.Inject

class FilmsRepositoryImp @Inject constructor(private  val  api: ApiFilms): FilmsRepository {
    override suspend fun getFilms(query: String):Response<FilmsList> {
        return api.getFilms(query)
    }

    override suspend fun getDetailsFilm(id: String): Response<FilmsDitails> {
        return api.getDetailsFilm(id)
    }
}