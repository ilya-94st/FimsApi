package com.example.fims.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fims.common.Resources
import com.example.fims.domain.model.FilmsList
import com.example.fims.domain.use_case.GetFilms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(private val getFilms: GetFilms): ViewModel() {

    private val _films: MutableStateFlow<Resources<FilmsList>> = MutableStateFlow(Resources.Loading())

    fun films(): StateFlow<Resources<FilmsList>> {
        return _films.asStateFlow()
    }

    fun searchFilms(query: String) = viewModelScope.launch {
        getFilms(query)
    }

    private suspend fun getFilms(query: String) {
            _films.value = Resources.Loading()
            val response = getFilms.invoke(query)
            _films.value = response
    }
}