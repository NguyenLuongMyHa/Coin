package com.myha.coin.ui.main.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.myha.coin.R
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.api.RetrofitBuilder
import com.myha.coin.data.db.CoinDatabase
import com.myha.coin.data.model.Coin
import com.myha.coin.ui.base.ViewModelFactory
import com.myha.coin.ui.main.viewmodel.MainViewModel
import com.myha.coin.utils.Status
import kotlinx.android.synthetic.main.fragment_coin_detail.*

class CoinDetailFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    var navController: NavController? = null
    
    
    private var coin : Coin? = null
    
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
        setupViewModel()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_coin_detail, container, false)
        setHasOptionsMenu(true);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupUI()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_edit -> {
                editCoin()
                return true
            }
            R.id.menu_item_delete -> {
                coin?.let { deleteCoin(it) }
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

        tv_coin_name.text = coin?.name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_coin_description.text = Html.fromHtml(coin?.description, Html.FROM_HTML_MODE_COMPACT);
        } else {
            tv_coin_description.text =  Html.fromHtml(coin?.description);
        }
        tv_coin_price.text = coin?.price.toString()
        tv_coin_symbol.text = coin?.symbol
        tv_coin_web_url.text = coin?.websiteUrl
        GlideToVectorYou.init().with(requireContext()).load(
            Uri.parse(coin?.iconUrl),
            img_coin_logo
        )
    }

    private fun editCoin() {
        val bundle = bundleOf(
            "coinId" to coin?.id,
            "coinName" to coin?.name,
            "coinSign" to coin?.sign,
            "coinSymbol" to coin?.symbol,
            "coinIcon" to coin?.iconUrl,
            "coinWeb" to coin?.websiteUrl,
            "coinPrice" to coin?.price,
            "coinDescription" to coin?.description,
            "action" to "EDIT"
        )
        navController!!.navigate(
            R.id.action_coinDetailFragment_to_newCoinFragment,
            bundle
        )
    }


    private fun deleteCoin(coin: Coin) {
        viewModel.deleteCoin(coin).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Delete Coin from Room Success",
                            Toast.LENGTH_LONG
                        ).show()
                        navController!!.navigate(R.id.action_coinDetailFragment_to_mainFragment)
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Delete Coin from Room Fail" + resource.message,
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