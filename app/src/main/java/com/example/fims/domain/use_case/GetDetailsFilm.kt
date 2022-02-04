package com.example.fims.domain.use_case

import com.example.fims.common.Resources
import com.example.fims.data.repository.FilmsRepositoryImp
import com.example.fims.domain.model.FilmsDitails
import javax.inject.Inject

class GetDetailsFilm @Inject constructor(
    private val repositoryImp: FilmsRepositoryImp ){

    suspend operator fun invoke(id: String): Resources<FilmsDitails> {
        return try {
            val response = repositoryImp.getDetailsFilm(id)
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