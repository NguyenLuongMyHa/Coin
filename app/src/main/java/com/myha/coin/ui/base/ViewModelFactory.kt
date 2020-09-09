package com.myha.coin.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.db.CoinDatabase
import com.myha.coin.data.repository.MainRepository
import com.myha.coin.ui.main.viewmodel.MainViewModel

class ViewModelFactory(private val apiHelper: ApiHelper, private val database: CoinDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper, database)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}