package com.example.films.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.films.domain.model.entinity.RemoteKeysFilms

@Dao
interface DaoRemoteKeys {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysFilms>)

    @Query("SELECT * FROM remote_films WHERE filmsId = :filmsId")
    suspend fun remoteKeysFilmsId(filmsId: String): RemoteKeysFilms?

    @Query("DELETE FROM remote_films")
    suspend fun clearRemoteKeys()
}