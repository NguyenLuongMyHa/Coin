package com.myha.coin.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.myha.coin.R
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.api.RetrofitBuilder
import com.myha.coin.data.db.CoinDatabase
import com.myha.coin.data.model.Coin
import com.myha.coin.data.model.NewCoin
import com.myha.coin.ui.base.ViewModelFactory
import com.myha.coin.ui.main.adapter.MainAdapter
import com.myha.coin.ui.main.viewmodel.MainViewModel
import com.myha.coin.utils.Status
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true);
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupUI()
        getCoinsFromLocal()
        setupAdapterClickListener()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_add -> {
                navController!!.navigate(R.id.action_mainFragment_to_newCoinFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService), CoinDatabase.getInstance(
                    requireContext()
                )
            )
        )[MainViewModel::class.java]
    }

    private fun setupUI() {
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupAdapterClickListener() {
        adapter.setUpListener(object : MainAdapter.ItemCLickedListener {
            override fun onItemClicked(coinDetail: Coin) {
                val bundle = bundleOf(
                    "coinId" to coinDetail.id,
                    "coinName" to coinDetail.name,
                    "coinSign" to coinDetail.sign,
                    "coinSymbol" to coinDetail.symbol,
                    "coinIcon" to coinDetail.iconUrl,
                    "coinWeb" to coinDetail.websiteUrl,
                    "coinPrice" to coinDetail.price,
                    "coinDescription" to coinDetail.description
                )
                navController!!.navigate(
                    R.id.action_mainFragment_to_coinDetailFragment,
                    bundle
                )
            }

        })
    }
    private fun getCoinsFromLocal() {
        viewModel.getCoinsLocal().observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Get Data From Room Success",
                            Toast.LENGTH_LONG
                        ).show()
                        resource.data?.let { res -> retrieveList(res) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Get Data From Room Fail",
                            Toast.LENGTH_LONG
                        ).show()
                        Toast.makeText(requireContext(), "Get Data From NetWork", Toast.LENGTH_LONG)
                            .show()

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
        viewModel.getCoins().observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Get Data From Network Success",
                            Toast.LENGTH_LONG
                        ).show()
                        resource.data?.let { res -> retrieveListFromNetwork(res.data.coins) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
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
        if(coins.isEmpty())
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

        viewModel.insertCoinsLocal(coins).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            "Get Coins Data From Network Success, Save Room",
                            Toast.LENGTH_LONG
                        ).show()
                        getCoinsFromLocal()
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}

