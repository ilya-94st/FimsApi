package com.example.films.domain.model.entinity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_films")
data class RemoteKeysFilms(
    @PrimaryKey
    val filmsId: String,
    val prevKey: Int?,
    val nextKey: Int?
)