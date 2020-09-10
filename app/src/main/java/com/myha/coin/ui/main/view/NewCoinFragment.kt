package com.myha.coin.ui.main.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.myha.coin.R
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.api.RetrofitBuilder
import com.myha.coin.data.db.CoinDatabase
import com.myha.coin.data.model.Coin
import com.myha.coin.data.model.NewCoin
import com.myha.coin.ui.base.ViewModelFactory
import com.myha.coin.ui.main.viewmodel.MainViewModel
import com.myha.coin.utils.Status
import kotlinx.android.synthetic.main.fragment_new_coin.*

class NewCoinFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    var navController: NavController? = null
    private var coin : Coin? = null
    private var action : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coin = arguments?.getLong("coinId")?.let {
            Coin(
                id = it,
                symbol = arguments?.getString("coinSymbol"),
                sign = arguments?.getString("coinSign"),
                name = arguments?.getString("coinName"),
                description = arguments?.getString("coinDescription"),
                iconUrl = arguments?.getString("coinIcon"),
                websiteUrl = arguments?.getString("coinWeb"),
                price = arguments?.getDouble("coinPrice")
            )
        }
        action = arguments?.getString("action")
        setupViewModel()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_new_coin, container, false)
        setHasOptionsMenu(true);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_coin, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_save -> {
                if (action != "EDIT") {
                    var newCoin = NewCoin()
                    //newCoin?.iconUrl = upload img
                    newCoin.description = tv_coin_description.text.toString()
                    newCoin.name = tv_coin_name.text.toString()
                    newCoin.symbol = tv_coin_symbol.text.toString()
                    newCoin.websiteUrl = tv_coin_web_url.text.toString()
                    newCoin.price = tv_coin_price.text.toString().toDouble()
                    saveNewCoin(newCoin)
                }
                else {
                    coin?.id?.let {
                        var editCoin = Coin(it)
                        //newCoin?.iconUrl = upload img
                        editCoin.description = tv_coin_description.text.toString()
                        editCoin.name = tv_coin_name.text.toString()
                        editCoin.symbol = tv_coin_symbol.text.toString()
                        editCoin.websiteUrl = tv_coin_web_url.text.toString()
                        editCoin.price = tv_coin_price.text.toString().toDouble()
                        editCoin(editCoin) }
                }
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
        if (action == "EDIT") {
            tv_coin_description.setText(coin?.description)
            tv_coin_name.setText(coin?.name)
            tv_coin_symbol.setText(coin?.symbol)
            tv_coin_web_url.setText(coin?.websiteUrl)
            tv_coin_price.setText(coin?.price?.toString())
        }
    }

    private fun saveNewCoin(coin: NewCoin) {
        viewModel.insertCoinLocal(coin).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Save Coin to Room Success",
                            Toast.LENGTH_LONG
                        ).show()
                        navController!!.navigate(R.id.action_newCoinFragment_to_mainFragment)
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Save Coin to Room Fail" + resource.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

    }

    private fun editCoin(coin: Coin) {
        viewModel.updateCoin(coin).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Edit Coin to Room Success",
                            Toast.LENGTH_LONG
                        ).show()
                        navController!!.navigate(R.id.action_newCoinFragment_to_mainFragment)
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Edit Coin to Room Fail" + resource.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

    }

}