package com.myha.coin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myha.coin.data.model.Animal
import com.myha.coin.data.model.PhotoConverter
import com.myha.coin.data.model.RemoteKeys

@Database(
    entities = [Animal::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PhotoConverter::class)
abstract class AnimalDatabase : RoomDatabase() {
    abstract fun animalsDao(): AnimalDao
    abstract fun remoteKeysDao(): RemoteKeysDao

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