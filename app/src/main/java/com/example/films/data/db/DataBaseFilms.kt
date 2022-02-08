package com.example.films.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.films.domain.model.entinity.EntityDetails
import com.example.films.domain.model.entinity.EntityFilms
import com.example.films.domain.model.entinity.RemoteKeysFilms

@Database(
    entities = [EntityFilms::class, RemoteKeysFilms::class, EntityDetails::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converter::class)
abstract class DataBaseFilms : RoomDatabase() {

    abstract fun getDaoFilms() : DaoFilms
    abstract fun remoteKeysDao(): DaoRemoteKeys
    abstract fun getDaoDetailsFilms(): DaoDetailsFilms
}