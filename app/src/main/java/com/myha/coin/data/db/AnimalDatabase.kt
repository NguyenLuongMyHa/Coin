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
abstract class AnimalDatabase : RoomDatabase() {
    abstract fun animalsDao(): AnimalDao

    companion object {
        @Volatile
        private var INSTANCE: AnimalDatabase? = null
        fun getInstance(context: Context) : AnimalDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AnimalDatabase::class.java,"Animal.db")
            .build()
    }
}