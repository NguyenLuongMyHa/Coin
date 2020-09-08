package com.myha.coin.data.model

import com.google.gson.annotations.SerializedName

data class CoinResponseData(
    @SerializedName("coins")
    val coins: List<Coin>,
    @SerializedName("coin")
    val coin: Coin,
    @SerializedName("base")
    val base: CoinBase
)