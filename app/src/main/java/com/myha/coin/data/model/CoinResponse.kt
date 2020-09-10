package com.myha.coin.data.model

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: CoinResponseData
)

data class CoinResponseData(
    @SerializedName("coins")
    val coins: List<Coin>,
    @SerializedName("coin")
    val coin: Coin,
    @SerializedName("base")
    val base: CoinBase
)


data class CoinBase(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("sign")
    val sign: String
)