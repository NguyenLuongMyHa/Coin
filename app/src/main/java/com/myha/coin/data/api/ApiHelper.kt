package com.myha.coin.data.api

import com.myha.coin.utils.Constant

class ApiHelper(private val apiService: ApiService) {

    suspend fun getCoin(id: Int) = apiService.getCoin(id)
    suspend fun getCoins() = apiService.getCoins()

    suspend fun getAnimals() = apiService.getAnimals("Bearer ".plus(Constant.AUTH))

}