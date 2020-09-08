package com.myha.coin.ui.main.adapter

import android.R.attr.fragment
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myha.coin.R
import com.myha.coin.data.model.Coin
import kotlinx.android.synthetic.main.item_coin.view.*


class MainAdapter(private val coins: ArrayList<Coin>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(coin: Coin?) {
            itemView.apply {
                if (coin != null) {
                    tv_coin_name.text = coin.name
                    tv_coin_price.text = coin.price?.toString()
                    tv_coin_symbol.text = coin.symbol
                    Glide.with(itemView.context)
                        .load(coin.iconUrl)
                        .override(60,60)
                        .centerCrop()
                        .into(itemView.img_coin)
                    tv_base_symbol.text = "USD"
                }

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