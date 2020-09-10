package com.myha.coin.data.repository

import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.db.CoinDatabase
import com.myha.coin.data.model.Coin
import com.myha.coin.data.model.NewCoin

class MainRepository(private val apiHelper: ApiHelper, private val database: CoinDatabase) {

    suspend fun getCoin(id: Int) = apiHelper.getCoin(id)
    suspend fun getCoins() = apiHelper.getCoins()
    suspend fun getCoinsLocal() = database.coinsDao().getCoins()
    suspend fun insertCoinsLocal(coins: List<Coin>) = database.coinsDao().insertAll(coins)
    suspend fun insertCoinLocal(coin: NewCoin) = database.coinsDao().insertOne(coin)
    suspend fun updateCoin(coin: Coin) = database.coinsDao().updateCoin(coin)
    suspend fun deleteCoin(coin: Coin) = database.coinsDao().deleteCoin(coin)

}