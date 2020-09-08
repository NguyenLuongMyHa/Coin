package com.myha.coin.ui.main.adapter

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

        fun bind(coin: Coin) {
            itemView.apply {
                textViewUserName.text = coin.name
                textViewUserEmail.text = coin.description
                Glide.with(imageViewAvatar.context)
                    .load(coin.iconUrl)
                    .into(imageViewAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false))

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