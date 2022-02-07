package com.example.films.data.remotemediator

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bumptech.glide.load.HttpException
import com.example.films.common.Constants
import com.example.films.data.api.ApiFilms
import com.example.films.data.db.DataBaseFilms
import com.example.films.data.downloadfiles.CreateFilesFilms
import com.example.films.data.toEntity
import com.example.films.domain.model.entinity.EntityFilms
import com.example.films.domain.model.entinity.RemoteKeysFilms
import java.io.File
import java.io.IOException

@ExperimentalPagingApi
class FilmsRemoteMediator (private val api: ApiFilms,
                           private val query: String,
                           private val db: DataBaseFilms,
                           private val app: Context
) : RemoteMediator<Int, EntityFilms>() {
    private var createFilesFilms: CreateFilesFilms? = null

    @SuppressLint("LongLogTag")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EntityFilms>
    ): MediatorResult {
        createFilesFilms = CreateFilesFilms(api, app)
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val apiResponse = api.getFilms(query = query, page)

            val films = apiResponse.body()!!.Search

            val entities = mutableListOf<EntityFilms>()

            films.forEach {
                val entity = it.toEntity()
                createFilesFilms!!.downloadArticle(entity)
                entities.add(entity)
            }

            val endOfPaginationReached = films.isEmpty()
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    val oldList = db.getDaoFilms().readOldListFilms()
                    oldList.forEach {
                        if(it.path.isNotEmpty()) {
                            try {
                                File(it.path).delete()
                            } catch (e: Exception){
                                Log.e("RemoteMediatorSearchNews", "$e")
                            }
                        }
                        db.remoteKeysDao().clearRemoteKeys()
                        db.getDaoFilms().deleteId(it.path)
                    }
                }
                val prevKey = if (page == Constants.STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = films.map {
                    RemoteKeysFilms(filmsId = it.imdbID, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys)
                entities.forEach {
                    db.getDaoFilms().insertFilms(it)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, EntityFilms>): RemoteKeysFilms? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { filmsId ->
                // Get the remote keys of the last item retrieved
                db.remoteKeysDao().remoteKeysFilmsId(filmsId.imdbID)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, EntityFilms>): RemoteKeysFilms? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { filmsId ->
                // Get the remote keys of the first items retrieved
                db.remoteKeysDao().remoteKeysFilmsId(filmsId.imdbID)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, EntityFilms>
    ): RemoteKeysFilms? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.imdbID?.let { filmsId ->
                db.remoteKeysDao().remoteKeysFilmsId(filmsId)
            }
        }
    }
}
