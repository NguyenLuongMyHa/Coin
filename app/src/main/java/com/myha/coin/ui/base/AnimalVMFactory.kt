package com.myha.coin.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.db.AnimalDatabase
import com.myha.coin.data.db.CoinDatabase
import com.myha.coin.data.repository.AnimalRepository
import com.myha.coin.data.repository.MainRepository
import com.myha.coin.ui.main.viewmodel.AnimalViewModel
import com.myha.coin.ui.main.viewmodel.MainViewModel

class AnimalVMFactory(private val apiHelper: ApiHelper, private val database: AnimalDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return AnimalViewModel(AnimalRepository(apiHelper, database)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}