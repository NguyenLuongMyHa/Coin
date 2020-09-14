package com.myha.coin.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.db.AnimalDatabase
import com.myha.coin.data.db.AnimalRemoteMediator
import com.myha.coin.data.model.Animal
import com.myha.coin.data.model.NewAnimal
import kotlinx.coroutines.flow.Flow

class AnimalRepository(private val apiHelper: ApiHelper, private val database: AnimalDatabase) {
    fun getSearchResultStream(query: String): Flow<PagingData<Animal>> {
        Log.d("AnimalRepository", "New query: $query")
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { database.animalsDao().getAnimalPagingSource()}

        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = AnimalRemoteMediator(
                query,
                apiHelper,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getToken() = apiHelper.getToken()
    suspend fun getAnimalsNetwork() = apiHelper.getAnimals()
    suspend fun findAnimalsByTypeNetwork(queryString: String) = apiHelper.findAnimalsByType(queryString)

    suspend fun getAnimalLocal() = database.animalsDao().getAnimals()
    suspend fun insertLocal(animal: NewAnimal) = database.animalsDao().insert(animal)
    suspend fun insertAllLocal(animal: List<Animal>) = database.animalsDao().insert(animal)
    suspend fun updateLocal(animal: Animal) = database.animalsDao().update(animal)
    suspend fun deleteLocal(animal: Animal) = database.animalsDao().delete(animal)

}