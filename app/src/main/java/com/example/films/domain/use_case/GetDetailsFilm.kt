package com.example.films.domain.use_case

import com.example.films.common.Resource
import com.example.films.domain.model.entinity.EntityDetails
import com.example.films.domain.repository.FilmsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailsFilm @Inject constructor(
    private val repository: FilmsRepository ){

    operator fun invoke(id: String): Flow<Resource<EntityDetails>> {
       return repository.getDetailsFilm(id)
    }
}