package com.example.films.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.films.domain.model.entinity.EntityDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoDetailsFilms {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilms(films: EntityDetails)

    @Query("delete from details")
    suspend fun deleteAllFilms()

    @Query("select * from details where imdb_id =:imdb_id")
    fun readDetailsFilms(imdb_id: String): Flow<EntityDetails>

    @Query("select * from details where imdb_id =:imdb_id")
    fun readOldListFilmsDetails(imdb_id: String): EntityDetails

    @Query("delete from details where imdb_id =:id")
    fun deleteId(id: String)
}