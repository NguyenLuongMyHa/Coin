package com.myha.coin.data.model

import com.google.gson.annotations.SerializedName

data class Coin(
    @SerializedName("id")
    val id: Int,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("sign")
    val sign: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("iconUrl")
    val iconUrl: String,
    @SerializedName("websiteUrl")
    val websiteUrl: String
)