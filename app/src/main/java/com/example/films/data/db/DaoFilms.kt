package com.example.films.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.films.domain.model.entinity.EntityFilms

@Dao
interface DaoFilms {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilms(films: EntityFilms)

    @Query("delete from films")
    suspend fun deleteAllFilms()

    @Query("select * from films order by id desc")
    fun readFilms(): PagingSource<Int, EntityFilms>

    @Query("select * from films")
    fun readOldListFilms(): List<EntityFilms>

    @Query("delete from films where path =:path")
    fun deleteId(path: String)
}