package com.example.films.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.films.domain.use_case.GetFilms
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(private val getFilms: GetFilms): ViewModel() {

    @ExperimentalPagingApi
    fun getFilms(query: String) = getFilms.invoke(query)
}