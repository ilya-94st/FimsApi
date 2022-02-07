package com.example.films.data.db

import androidx.room.TypeConverter
import com.example.films.domain.model.response.Rating

class Converter {

    @TypeConverter
    fun fromRating(rating: Rating): String {
        return rating.Value
    }

    @TypeConverter
    fun toRating(rating: String): Rating {
        return Rating(rating, rating)
    }
}