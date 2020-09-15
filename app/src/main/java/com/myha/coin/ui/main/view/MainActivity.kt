package com.myha.coin.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.myha.coin.R
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.api.RetrofitBuilder
import com.myha.coin.data.db.AnimalDatabase
import com.myha.coin.ui.base.AnimalVMFactory
import com.myha.coin.ui.main.viewmodel.AnimalViewModel
import com.myha.coin.utils.Constant
import com.myha.coin.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: AnimalViewModel
    private var isAuth = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        if(!isAuth)
            getApiToken()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, AnimalVMFactory(
            ApiHelper(RetrofitBuilder.apiService), AnimalDatabase.getInstance(
                this
            )
        )
        ).get(AnimalViewModel::class.java)
    }

    private fun getApiToken() {
        viewModel.getToken().observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Constant.AUTH = resource.data?.access_token ?: Constant.AUTH
                        isAuth = true
                        Log.i("PetFinder", "Auth Success ".plus(Constant.AUTH))
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Log.i("PetFinder", "Auth Fail ".plus(resource.message))
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })

    }
}