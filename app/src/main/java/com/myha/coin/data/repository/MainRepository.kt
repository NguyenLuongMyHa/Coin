package com.myha.coin.data.repository

import com.myha.coin.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getCoin(id: Int) = apiHelper.getCoin(id)
    suspend fun getCoins() = apiHelper.getCoins()

}