package com.myha.coin.data.repository

import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.db.CoinDatabase
import com.myha.coin.data.model.Coin

class MainRepository(private val apiHelper: ApiHelper, private val database: CoinDatabase) {

    suspend fun getCoin(id: Int) = apiHelper.getCoin(id)
    suspend fun getCoins() = apiHelper.getCoins()
    suspend fun getCoinsLocal() = database.coinsDao().getCoins()
    suspend fun insertCoinsLocal(coins: List<Coin>) = database.coinsDao().insertAll(coins)

}