package com.myha.coin.data.api

import com.myha.coin.utils.Constant

class ApiHelper(private val apiService: ApiService) {
    suspend fun getToken() = apiService.generateToken("client_credentials",Constant.CLIENT_ID, Constant.CLIENT_SECRET)

    suspend fun getAnimals(query: String = "", page: Int = 1) = apiService.getAnimals("Bearer ".plus(Constant.AUTH), query, page)
    suspend fun findAnimalsByType(queryString: String) = apiService.findAnimalsByType("Bearer ".plus(Constant.AUTH), queryString)
}