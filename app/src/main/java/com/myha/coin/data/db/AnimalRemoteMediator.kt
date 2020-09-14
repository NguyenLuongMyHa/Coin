package com.myha.coin.data.db

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.model.Animal
import com.myha.coin.data.model.RemoteKeys
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class AnimalRemoteMediator(
    private val query: String,
    private val service: ApiHelper,
    private val animalDatabase: AnimalDatabase
) : RemoteMediator<Int, Animal>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Animal>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null || remoteKeys.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }
        val apiQuery = query

        try {
            val apiResponse = service.getAnimals(apiQuery, page)

            val animals = apiResponse.animals
            val endOfPaginationReached = animals.isEmpty()
            animalDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    animalDatabase.remoteKeysDao().clearRemoteKeys()
                    animalDatabase.animalsDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                Log.i("PetFinder - prev", prevKey.toString())
                Log.i("PetFinder - next", nextKey.toString())

                val keys = animals.map {
                    RemoteKeys(animalId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                animalDatabase.remoteKeysDao().insertAll(keys)
                animalDatabase.animalsDao().insert(animals)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Animal>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { animalId ->
                animalDatabase.remoteKeysDao().remoteKeysAnimalId(animalId)
            }
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Animal>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { animal ->
                animalDatabase.remoteKeysDao().remoteKeysAnimalId(animal.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Animal>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { animal ->
                animalDatabase.remoteKeysDao().remoteKeysAnimalId(animal.id)
            }
    }
}