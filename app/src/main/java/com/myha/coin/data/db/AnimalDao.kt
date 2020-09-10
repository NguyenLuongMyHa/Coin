package com.myha.coin.data.db

import androidx.room.*
import com.myha.coin.data.model.Animal
import com.myha.coin.data.model.Coin
import com.myha.coin.data.model.NewCoin

@Dao
interface AnimalDao {

    @Insert(entity = Animal::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(animal: Animal)

    @Query("SELECT * FROM animals ORDER BY id ASC")
    suspend fun getAnimals(): List<Animal>

    @Query("SELECT * FROM animals WHERE type LIKE :queryString OR name LIKE :queryString OR description LIKE :queryString")
    suspend fun find(queryString: String): List<Animal>

    @Update
    suspend fun update(animal: Animal)

    @Query("DELETE FROM animals")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(animal: Animal)
}