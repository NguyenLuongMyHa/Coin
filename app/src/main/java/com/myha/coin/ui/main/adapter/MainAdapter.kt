package com.myha.coin.ui.main.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.myha.coin.R
import com.myha.coin.data.model.Coin
import com.myha.coin.ui.main.view.CoinDetailFragment
import kotlinx.android.synthetic.main.item_coin.view.*


class MainAdapter(private val coins: ArrayList<Coin>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(coin: Coin?) {

            GlideToVectorYou.init().with(itemView.context).load(
                Uri.parse(coin?.iconUrl),
                itemView.img_coin
            );
            itemView.apply {
                if (coin != null) {
                    tv_coin_name.text = coin.name
                    tv_coin_price.text = coin.price?.toString()
                    tv_coin_symbol.text = coin.symbol
                    tv_base_symbol.text = "USD"
                }
            }
            itemView.setOnClickListener {
                navigateToCoinDetail(coin)
            }
        }
        private fun navigateToCoinDetail(coin: Coin?) {
            coin?.let {
                val fragment: Fragment = CoinDetailFragment()
                val fragmentManager: FragmentManager =
                    (itemView.context as FragmentActivity).supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frag_view, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_coin,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = coins.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(coins[position])
    }

    fun addCoins(coins: List<Coin>) {
        this.coins.apply {
            clear()
            addAll(coins)
        }
    }
}