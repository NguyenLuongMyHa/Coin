package com.myha.coin.data.db

import androidx.room.*
import com.myha.coin.data.model.Coin
import com.myha.coin.data.model.NewCoin

@Dao
interface CoinDao {

    @Insert(entity = Coin::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(coin: NewCoin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<Coin>)

    @Query("SELECT * FROM coins ORDER BY id ASC")
    suspend fun getCoins(): List<Coin>

    @Query("SELECT * FROM coins WHERE name LIKE :queryString OR description LIKE :queryString")
    suspend fun find(queryString: String): List<Coin>

    @Update
    suspend fun updateCoin(coin: Coin)

    @Update
    suspend fun updateCoins(coins: List<Coin>);

    @Query("DELETE FROM coins")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteCoin(coins: Coin)
}