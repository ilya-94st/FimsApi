package com.example.films.domain.use_case

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.films.domain.model.entinity.EntityFilms
import com.example.films.domain.repository.FilmsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilms @Inject constructor(private val filmsRepository: FilmsRepository) {


    @ExperimentalPagingApi
    operator fun invoke(query: String): Flow<PagingData<EntityFilms>> {
        return filmsRepository.getFilms(query)
   }
}