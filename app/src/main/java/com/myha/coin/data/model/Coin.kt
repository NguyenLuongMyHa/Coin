package com.myha.coin.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "coins")
data class Coin(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") @field:SerializedName("id")
    val id: Long,
    @ColumnInfo(name = "symbol") @field:SerializedName("symbol")
    var symbol: String? = "",
    @ColumnInfo(name = "sign") @field:SerializedName("sign")
    var sign: String? = "",
    @ColumnInfo(name = "name") @field:SerializedName("name")
    var name: String? = "",
    @ColumnInfo(name = "description") @field:SerializedName("description")
    var description: String? = "",
    @ColumnInfo(name = "iconUrl") @field:SerializedName("iconUrl")
    var iconUrl: String? = "",
    @ColumnInfo(name = "websiteUrl") @field:SerializedName("websiteUrl")
    var websiteUrl: String? = "",
    @ColumnInfo(name = "price") @field:SerializedName("price")
    var price: Double? = 0.0

)

data class NewCoin (
    var symbol: String? = "",
    var sign: String? = "",
    var name: String? = "",
    var description: String? = "",
    var iconUrl: String? = "",
    var websiteUrl: String? = "",
    var price: Double? = 0.0
)