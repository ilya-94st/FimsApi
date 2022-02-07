package com.example.films.data

import com.example.films.domain.model.entinity.EntityFilms
import com.example.films.domain.model.response.Films


    fun Films.toEntity(): EntityFilms {
        return EntityFilms(id = 0, Poster, Title, Type,  Year, imdbID, path = "")
    }
