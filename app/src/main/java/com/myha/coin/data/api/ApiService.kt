package com.myha.coin.data.api

import com.myha.coin.data.model.AnimalResponse
import com.myha.coin.data.model.CoinResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("coin/{coin_id}/")
    suspend fun getCoin(@Path(value = "coin_id") id: Int): CoinResponse
    @GET("coins/")
    suspend fun getCoins(): CoinResponse
    @GET("animals")
    suspend fun getAnimals(@Header("Authorization") authorization: String): AnimalResponse
}