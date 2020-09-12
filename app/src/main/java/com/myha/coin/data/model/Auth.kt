package com.myha.coin.data.model

import com.google.gson.annotations.SerializedName

data class Auth(
    @SerializedName("access_token")
    val access_token: String
)
