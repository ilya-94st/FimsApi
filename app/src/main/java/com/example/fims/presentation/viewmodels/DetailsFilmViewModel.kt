package com.example.fims.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fims.common.Resources
import com.example.fims.domain.model.FilmsDitails
import com.example.fims.domain.use_case.GetDetailsFilm
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