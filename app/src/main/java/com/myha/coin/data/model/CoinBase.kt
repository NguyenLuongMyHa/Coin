package com.myha.coin.data.model

import com.google.gson.annotations.SerializedName

data class CoinBase(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("sign")
    val sign: String
)