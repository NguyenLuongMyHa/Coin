package com.myha.coin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myha.coin.R
import kotlinx.android.synthetic.main.item_footer.view.*

class AnimalLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<AnimalLoadStateAdapter.AnimalLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: AnimalLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): AnimalLoadStateViewHolder {
        return AnimalLoadStateViewHolder.create(parent, retry)
    }

    class AnimalLoadStateViewHolder(
        view: View, retry: () -> Unit
    ) : RecyclerView.ViewHolder(view) {

        init {
            itemView.retry_button.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                itemView.error_msg.text = loadState.error.localizedMessage
            }
            itemView.progress_bar.isVisible = loadState is LoadState.Loading
            itemView.retry_button.isVisible = loadState !is LoadState.Loading
            itemView.error_msg.isVisible = loadState !is LoadState.Loading
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): AnimalLoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer, parent, false)
                return AnimalLoadStateViewHolder(view, retry)
            }
        }
    }
}

