package com.myha.coin.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.myha.coin.data.model.Animal
import com.myha.coin.data.model.Coin
import com.myha.coin.data.model.NewCoin
import com.myha.coin.data.repository.AnimalRepository
import com.myha.coin.data.repository.MainRepository
import com.myha.coin.utils.Resource
import kotlinx.coroutines.Dispatchers

class AnimalViewModel(private val animalRepository: AnimalRepository) : ViewModel() {

    fun getAnimalsNetwork() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.getAnimalsNetwork()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun insertLocal(animal: Animal) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.insertLocal(animal)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun updateLocal(animal: Animal) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.updateLocal(animal)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun deleteLocal(animal: Animal) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.deleteLocal(animal)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getAnimalLocal() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.getAnimalLocal()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}