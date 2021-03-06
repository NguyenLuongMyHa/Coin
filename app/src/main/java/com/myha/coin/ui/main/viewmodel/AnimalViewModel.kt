package com.myha.coin.ui.main.viewmodel

import android.app.DownloadManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.myha.coin.data.model.Animal
import com.myha.coin.data.model.NewAnimal
import com.myha.coin.data.repository.AnimalRepository
import com.myha.coin.utils.Resource
import kotlinx.coroutines.Dispatchers

class AnimalViewModel(private val animalRepository: AnimalRepository) : ViewModel() {

    fun getToken() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.getToken()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getAnimalsNetwork() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.getAnimalsNetwork()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun findAnimalsByTypeNetwork(queryString: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.findAnimalsByTypeNetwork(queryString)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun insertLocal(animal: NewAnimal) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.insertLocal(animal)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
    fun insertAllLocal(animals: List<Animal>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = animalRepository.insertAllLocal(animals)))
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