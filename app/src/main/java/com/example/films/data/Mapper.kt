package com.example.films.data

import com.example.films.domain.model.entinity.EntityDetails
import com.example.films.domain.model.entinity.EntityFilms
import com.example.films.domain.model.response.Films
import com.example.films.domain.model.response.FilmsDitails


fun Films.toEntity(): EntityFilms {
        return EntityFilms(id = 0, Poster, Title, Type,  Year, imdbID, path = "")
    }

fun FilmsDitails.toEntityDetails(): EntityDetails {
    return EntityDetails(id = 0, Actors, Awards, BoxOffice, Country, DVD, Director, Genre,Language, Metascore
    , Plot, Poster, Production, Rated, Released, Response, Runtime, Title, Type, Website, Writer, Year
    , path = "", imdbVotes = imdbVotes, imdbRating = imdbRating, imdbID = imdbID
    )
}