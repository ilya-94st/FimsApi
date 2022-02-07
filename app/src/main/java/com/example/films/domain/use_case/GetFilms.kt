package com.example.films.domain.use_case

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.films.data.repository.FilmsRepositoryImp
import com.example.films.domain.model.entities.FilmEntities
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilms @Inject constructor(private val filmsRepository: FilmsRepositoryImp) {


    @ExperimentalPagingApi
    operator fun invoke(query: String): Flow<PagingData<FilmEntities>> {
        return filmsRepository.getFilms(query)
   }
}