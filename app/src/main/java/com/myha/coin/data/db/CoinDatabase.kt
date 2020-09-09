package com.myha.coin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myha.coin.data.model.Coin

@Database(
    entities = [Coin::class],
    version = 2,
    exportSchema = false
)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinsDao(): CoinDao

    companion object {
        @Volatile
        private var INSTANCE: CoinDatabase? = null
        fun getInstance(context: Context) : CoinDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            CoinDatabase::class.java,"Coin.db")
            .build()
    }
}