package com.myha.coin.ui.main.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.myha.coin.R
import kotlinx.android.synthetic.main.fragment_coin_detail.*
import kotlinx.android.synthetic.main.item_coin.view.*

class CoinDetailFragment : Fragment() {
    private var symbol: String? = ""
    private var sign: String? = ""
    private var name: String? = ""
    private var description: String? = ""
    private var iconUrl: String? = ""
    private var websiteUrl: String? = ""
    private var price: Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = arguments?.getString("coinName")
        sign = arguments?.getString("coinSign")
        symbol = arguments?.getString("coinSymbol")
        iconUrl = arguments?.getString("coinIcon")
        websiteUrl = arguments?.getString("coinWeb")
        price = arguments?.getDouble("coinPrice")
        description = arguments?.getString("coinDescription")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_coin_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_coin_name.text = name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_coin_description.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT);
        } else {
            tv_coin_description.text =  Html.fromHtml(description);
        }
        tv_coin_price.text = price.toString()
        tv_coin_symbol.text = symbol
        tv_coin_web_url.text = websiteUrl
        GlideToVectorYou.init().with(requireContext()).load(
            Uri.parse(iconUrl),
            img_coin_logo
        )
    }
}