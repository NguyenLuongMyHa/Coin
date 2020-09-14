package com.myha.coin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val animalId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)