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
    val symbol: String? = "",
    @ColumnInfo(name = "sign") @field:SerializedName("sign")
    val sign: String? = "",
    @ColumnInfo(name = "name") @field:SerializedName("name")
    val name: String? = "",
    @ColumnInfo(name = "description") @field:SerializedName("description")
    val description: String? = "",
    @ColumnInfo(name = "iconUrl") @field:SerializedName("iconUrl")
    val iconUrl: String? = "",
    @ColumnInfo(name = "websiteUrl") @field:SerializedName("websiteUrl")
    val websiteUrl: String? = "",
    @ColumnInfo(name = "price") @field:SerializedName("price")
    val price: Double?

)