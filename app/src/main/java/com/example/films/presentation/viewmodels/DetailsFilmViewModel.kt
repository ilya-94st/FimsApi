package com.example.films.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.films.common.Resources
import com.example.films.domain.model.response.FilmsDitails
import com.example.films.domain.use_case.GetDetailsFilm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFilmViewModel @Inject constructor(private val getDetailsFilm: GetDetailsFilm): ViewModel() {

    private val _details: MutableStateFlow<Resources<FilmsDitails>> = MutableStateFlow(Resources.Loading())

    fun details(): StateFlow<Resources<FilmsDitails>> {
        return _details.asStateFlow()
    }

    fun getDetailsFilm(id: String) = viewModelScope.launch {
        getFilms(id)
    }

    private suspend fun getFilms(id: String) {
        _details.value = Resources.Loading()
        val response = getDetailsFilm.invoke(id)
        _details.value = response
    }
}