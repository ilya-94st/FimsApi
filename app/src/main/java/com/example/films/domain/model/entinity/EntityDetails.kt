package com.example.films.domain.model.entinity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "details")
data class EntityDetails(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "actors") val Actors: String,
    @ColumnInfo(name = "awards") val Awards: String,
    @ColumnInfo(name = "box_office") val BoxOffice: String,
    @ColumnInfo(name = "country") val Country: String,
    @ColumnInfo(name = "dvd") val DVD: String,
    @ColumnInfo(name = "director") val Director: String,
    @ColumnInfo(name = "genre") val Genre: String,
    @ColumnInfo(name = "language") val Language: String,
    @ColumnInfo(name = "metascore") val Metascore: String,
    @ColumnInfo(name = "plot") val Plot: String,
    @ColumnInfo(name = "poster") val Poster: String,
    @ColumnInfo(name = "production") val Production: String,
    @ColumnInfo(name = "rated") val Rated: String,
    @ColumnInfo(name = "released") val Released: String,
    @ColumnInfo(name = "response") val Response: String,
    @ColumnInfo(name = "runtime") val Runtime: String,
    @ColumnInfo(name = "title") val Title: String,
    @ColumnInfo(name = "type") val Type: String,
    @ColumnInfo(name = "website") val Website: String,
    @ColumnInfo(name = "writer") val Writer: String,
    @ColumnInfo(name = "year") val Year: String,
    @ColumnInfo(name = "imdb_id") val imdbID: String,
    @ColumnInfo(name = "imd_rating") val imdbRating: String,
    @ColumnInfo(name = "imd_votes") val imdbVotes: String,
    @ColumnInfo(name = "path") val path: String
)