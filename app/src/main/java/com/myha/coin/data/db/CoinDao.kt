package com.myha.coin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myha.coin.data.model.Coin

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(coin: Coin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<Coin>)

    @Query("SELECT * FROM coins ORDER BY id ASC")
    suspend fun getCoins(): List<Coin>

    @Query("SELECT * FROM coins WHERE name LIKE :queryString OR description LIKE :queryString")
    suspend fun find(queryString: String): List<Coin>

    @Query("DELETE FROM coins")
    suspend fun deleteAll()
}