package com.example.films.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.example.films.common.Constants
import com.example.films.common.Resource
import com.example.films.data.api.ApiFilms
import com.example.films.data.db.DataBaseFilms
import com.example.films.data.network.networkBoundResource
import com.example.films.data.remotemediator.FilmsRemoteMediator
import com.example.films.data.toEntityDetails
import com.example.films.domain.model.entinity.EntityDetails
import com.example.films.domain.model.entinity.EntityFilms
import com.example.films.domain.repository.FilmsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilmsRepositoryImp @Inject constructor(
    private var db: DataBaseFilms,
    private val api: ApiFilms,
    private val app: Context
): FilmsRepository {
    private val pagingSourceFactoryForFilms =  { db.getDaoFilms().readFilms()}


    @ExperimentalPagingApi
    override fun getFilms(query: String): Flow<PagingData<EntityFilms>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = FilmsRemoteMediator(
                api,
                query,
                db,
                app
            ),
            pagingSourceFactory = pagingSourceFactoryForFilms
        ).flow
    }

    override fun getDetailsFilm(id: String): Flow<Resource<EntityDetails>> {
        return networkBoundResource(
            query = {
                db.getDaoDetailsFilms().readDetailsFilms(id)
            },
            fetch = {
                api.getDetailsFilm(id)
            },
            saveFetchResult = { result ->

                val apiResponse = result.body()

                val entities = mutableListOf<EntityDetails>()

                apiResponse?.imdbID = id

                if (apiResponse != null) {
                    entities.add(apiResponse.toEntityDetails())
                }

                db.withTransaction {
                    db.getDaoDetailsFilms().deleteId(id)
                    entities.forEach {
                        db.getDaoDetailsFilms().insertFilms(it)
                    }
                }
            }
        )
    }


}