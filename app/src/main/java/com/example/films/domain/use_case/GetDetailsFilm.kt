package com.example.films.domain.use_case

import com.example.films.common.Resources
import com.example.films.domain.model.response.FilmsDitails
import com.example.films.domain.repository.FilmsRepository
import javax.inject.Inject

class GetDetailsFilm @Inject constructor(
    private val repository: FilmsRepository ){

    suspend operator fun invoke(id: String): Resources<FilmsDitails> {
        return try {
            val response = repository.getDetailsFilm(id)
            val result = response.body()
            if(response.isSuccessful && result != null) {
                Resources.Success(result)
            } else {
                Resources.Error(response.message())
            }
        } catch(e: Exception) {
            Resources.Error(e.message ?: "An error occurred")
        }
    }
}