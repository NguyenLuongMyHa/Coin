package com.myha.coin.ui.main.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.myha.coin.R
import com.myha.coin.data.model.Coin
import kotlinx.android.synthetic.main.item_coin.view.*


class MainAdapter(private val coins: ArrayList<Coin>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {
    private lateinit var mItemCLicked: ItemCLickedListener

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
        holder.itemView.setOnClickListener {
            mItemCLicked.let {
                mItemCLicked.onItemClicked(coins[position])
            }
        }
    }

    fun addCoins(coins: List<Coin>) {
        this.coins.apply {
            clear()
            addAll(coins)
        }
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(coin: Coin?) {

            GlideToVectorYou.init().with(itemView.context).load(
                Uri.parse(coin?.iconUrl),
                itemView.img_coin
            )
            itemView.apply {
                if (coin != null) {
                    tv_coin_name.text = coin.name
                    tv_coin_price.text = coin.price?.toString()
                    tv_coin_symbol.text = coin.symbol
                    tv_base_symbol.text = "USD"
                }
            }

        }
    }

    interface ItemCLickedListener {
        fun onItemClicked(coinDetail: Coin)
    }
    fun setUpListener(itemCLicked: ItemCLickedListener){
        mItemCLicked = itemCLicked
    }
}