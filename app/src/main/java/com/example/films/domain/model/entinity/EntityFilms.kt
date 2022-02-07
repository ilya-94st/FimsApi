package com.example.films.domain.model.entinity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class EntityFilms(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "poster") val Poster: String?,
    @ColumnInfo(name = "title") val Title: String,
    @ColumnInfo(name = "type") val Type: String,
    @ColumnInfo(name = "year") val Year: String,
    @ColumnInfo(name = "imdb_id") val imdbID: String,
    @ColumnInfo(name = "path") var path: String
)
