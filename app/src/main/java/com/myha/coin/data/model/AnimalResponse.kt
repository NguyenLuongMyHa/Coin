package com.myha.coin.data.model

import com.google.gson.annotations.SerializedName

data class AnimalResponse(
    @SerializedName("animals")
    val animals: List<Animal>,
    @SerializedName("pagination")
    val pagination: Pagination
)

data class Pagination(
    @SerializedName("count_per_page")
    var count_per_page: Int,
    @SerializedName("total_count")
    var total_count: Int,
    @SerializedName("current_page")
    var current_page: Int,
    @SerializedName("total_pages")
    var total_pages: Int
)
