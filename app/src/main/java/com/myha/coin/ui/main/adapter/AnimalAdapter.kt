package com.myha.coin.ui.main.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myha.coin.R.layout.item_animal
import com.myha.coin.data.model.Animal
import kotlinx.android.synthetic.main.item_animal.view.*


class AnimalAdapter(private val animals: ArrayList<Animal>) : RecyclerView.Adapter<AnimalAdapter.DataViewHolder>() {
    private lateinit var mItemCLicked: ItemCLickedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                item_animal,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = animals.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(animals[position])
        holder.itemView.setOnClickListener {
            mItemCLicked.let {
                mItemCLicked.onItemClicked(animals[position])
            }
        }
    }

    fun addAnimals(animals: List<Animal>) {
        this.animals.apply {
            clear()
            addAll(animals)
        }
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(animal: Animal) {
            Glide.with(itemView.context).load(animal.photos?.get(0)?.fullsize).into(itemView.img_animal)

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

    interface ItemCLickedListener {
        fun onItemClicked(animal: Animal)
    }
    fun setUpListener(itemCLicked: ItemCLickedListener){
        mItemCLicked = itemCLicked
    }
}