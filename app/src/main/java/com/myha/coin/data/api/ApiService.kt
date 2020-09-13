package com.myha.coin.data.api

import com.myha.coin.data.model.AnimalResponse
import com.myha.coin.data.model.Auth
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun generateToken(
        @Field("grant_type") grantType: String?,
        @Field("client_id") clientId: String?,
        @Field("client_secret") clientSecret: String?,
    ) : Auth

    @GET("animals")
    suspend fun getAnimals(@Header("Authorization") authorization: String): AnimalResponse
    @GET("animals")
    suspend fun findAnimalsByType(
        @Header("Authorization") authorization: String,
        @Query("type") queryString: String
    ): AnimalResponse

}