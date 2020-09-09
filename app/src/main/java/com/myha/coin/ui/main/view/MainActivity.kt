package com.myha.coin.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.myha.coin.R
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.api.RetrofitBuilder
import com.myha.coin.ui.base.ViewModelFactory
import com.myha.coin.ui.main.viewmodel.MainViewModel
import com.myha.coin.utils.Status
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.myha.coin.data.db.CoinDatabase
import com.myha.coin.data.model.Coin
import com.myha.coin.ui.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()
        getCoinsFromLocal()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService), CoinDatabase.getInstance(this))
        )[MainViewModel::class.java]
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun getCoinsFromLocal() {
        viewModel.getCoinsLocal().observe(this, {
            it?.let {
                resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Get Data From Room Success", Toast.LENGTH_LONG).show()
                        resource.data?.let { res -> retrieveList(res) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Get Data From Room Fail", Toast.LENGTH_LONG).show()
                        Toast.makeText(this, "Get Data From NetWork", Toast.LENGTH_LONG).show()

                        getCoinsFromNetwork()

                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })

    }

    private fun getCoinsFromNetwork() {
        viewModel.getCoins().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Get Data From Network Success", Toast.LENGTH_LONG).show()
                        resource.data?.let { res -> retrieveListFromNetwork(res.data.coins) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        Log.e("MYHA", it.message.toString())
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }
    private fun retrieveList(coins: List<Coin>) {
        if(coins.size == 0)
            getCoinsFromNetwork()
        else
        {
            adapter.apply {
                addCoins(coins)
                notifyDataSetChanged()
            }
        }
    }

    private fun retrieveListFromNetwork(coins: List<Coin>) {
        viewModel.insertCoinsLocal(coins).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this, "Get Coins Data From Network Success, Save Room", Toast.LENGTH_LONG).show()
                        getCoinsFromLocal()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}