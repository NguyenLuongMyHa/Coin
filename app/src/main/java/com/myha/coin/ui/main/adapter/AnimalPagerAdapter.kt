package com.myha.coin.ui.main.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myha.coin.R
import com.myha.coin.data.model.Animal
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalPagerAdapter : PagingDataAdapter<Animal, RecyclerView.ViewHolder> (COMPARATOR) {
    private lateinit var mItemCLicked: AnimalPagerAdapter.ItemCLickedListener

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val animal = getItem(position)
        if (animal != null) {
            (holder as AnimalViewHolder).bind(animal)
        }
        (holder as AnimalViewHolder).itemView.setOnClickListener {
            mItemCLicked.let {
                if (animal != null) {
                    mItemCLicked.onItemClicked(animal)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnimalViewHolder.create(parent)
    }
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Animal>() {
            override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean =
                oldItem == newItem
        }
    }
    class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var animal: Animal? = null

        fun bind(animal: Animal?) {
            if(animal == null) {
                val resources = itemView.resources
            }
            else {
                this.animal = animal

                if(animal.photos?.size != 0)
                    Glide.with(itemView.context).load(animal.photos?.get(0)?.fullsize).into(itemView.img_animal)
                else
                    Glide.with(itemView.context).load(R.drawable.ic_image)
                        .into(itemView.img_animal);
                itemView.apply {
                    //set text
                    tv_age.text = animal.age
                    tv_gender.text = animal.gender
                    tv_name.text = animal.name
                    tv_size.text = animal.size
                    tv_type.text = animal.type
                }
            }
        }
        companion object {
            fun create(parent: ViewGroup): AnimalViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_animal, parent, false)
                return AnimalViewHolder(view)
            }
        }
    }
    interface ItemCLickedListener {
        fun onItemClicked(animal: Animal)
    }
    fun setUpListener(itemCLicked: ItemCLickedListener){
        mItemCLicked = itemCLicked
    }
}