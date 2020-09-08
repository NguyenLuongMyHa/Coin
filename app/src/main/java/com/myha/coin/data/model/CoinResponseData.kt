package com.myha.coin.data.model

import com.google.gson.annotations.SerializedName

data class CoinResponseData(
    @SerializedName("coins")
    val coins: List<Coin>
)