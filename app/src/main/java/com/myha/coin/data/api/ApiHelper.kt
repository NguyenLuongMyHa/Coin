package com.myha.coin.data.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getCoin(id: Int) = apiService.getCoin(id)
    suspend fun getCoins() = apiService.getCoins()

}