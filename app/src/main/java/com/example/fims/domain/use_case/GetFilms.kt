package com.example.fims.domain.use_case

import com.example.fims.common.Resources
import com.example.fims.data.repository.FilmsRepositoryImp
import com.example.fims.domain.model.FilmsDitails
import com.example.fims.domain.model.FilmsList
import javax.inject.Inject

class GetFilms @Inject constructor(private val filmsRepository: FilmsRepositoryImp) {

   suspend operator fun invoke(query: String): Resources<FilmsList> {
       return try {
           val response = filmsRepository.getFilms(query)
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