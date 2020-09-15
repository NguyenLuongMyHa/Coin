package com.myha.coin.data.model

import com.google.gson.annotations.SerializedName

data class AnimalResponse(
    @SerializedName("animals")
    var animals: List<Animal> = emptyList(),
)