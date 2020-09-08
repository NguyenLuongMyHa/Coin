package com.myha.coin.data.model

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: CoinResponseData
)