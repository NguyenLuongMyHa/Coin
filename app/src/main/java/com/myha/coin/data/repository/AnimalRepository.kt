package com.myha.coin.data.repository

import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.db.AnimalDatabase
import com.myha.coin.data.db.CoinDatabase
import com.myha.coin.data.model.Animal
import com.myha.coin.data.model.Coin
import com.myha.coin.data.model.NewCoin

class AnimalRepository(private val apiHelper: ApiHelper, private val database: AnimalDatabase) {

    suspend fun getAnimalsNetwork() = apiHelper.getAnimals()
    suspend fun getAnimalLocal() = database.animalsDao().getAnimals()
    suspend fun insertLocal(animal: Animal) = database.animalsDao().insert(animal)
    suspend fun updateLocal(animal: Animal) = database.animalsDao().update(animal)
    suspend fun deleteLocal(animal: Animal) = database.animalsDao().delete(animal)

}