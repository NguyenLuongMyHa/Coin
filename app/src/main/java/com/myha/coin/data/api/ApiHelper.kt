package com.myha.coin.data.api

import com.myha.coin.utils.Constant

class ApiHelper(private val apiService: ApiService) {
    companion object {
        val AUTH: String = "Bearer ".plus(Constant.AUTH)
    }
    suspend fun getToken() = apiService.generateToken("client_credentials",Constant.CLIENT_ID, Constant.CLIENT_SECRET)

    suspend fun getAnimals(query: String = "", page: Int) = apiService.getAnimals(AUTH, query, page)
    suspend fun findAnimalsByType(queryString: String) = apiService.findAnimalsByType(AUTH, queryString)
}