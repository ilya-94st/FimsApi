package com.example.films.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.films.domain.use_case.GetDetailsFilm
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsFilmViewModel @Inject constructor(private val getDetailsFilm: GetDetailsFilm): ViewModel() {

   fun getFilmsDetails(id: String) = getDetailsFilm.invoke(id).asLiveData()

}