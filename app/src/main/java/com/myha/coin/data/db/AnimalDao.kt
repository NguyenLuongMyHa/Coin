package com.myha.coin.data.db

import androidx.room.*
import com.myha.coin.data.model.Animal
import com.myha.coin.data.model.NewAnimal

@Dao
interface AnimalDao {

    @Insert(entity = Animal::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(animal: NewAnimal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(animals: List<Animal>)

    @Query("SELECT * FROM animals ORDER BY id ASC")
    suspend fun getAnimals(): List<Animal>

    @Update
    suspend fun update(animal: Animal)

    @Query("DELETE FROM animals")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(animal: Animal)
}