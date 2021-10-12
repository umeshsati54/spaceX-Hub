package com.usati.spacex.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.usati.spacex.R
import com.usati.spacex.models.rocket.Rocket
import kotlinx.android.synthetic.main.rocket_item.view.*

class RocketAdapter : RecyclerView.Adapter<RocketAdapter.RocketViewHolder>() {
    inner class RocketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Rocket>() {
        override fun areItemsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketViewHolder {
        return RocketViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rocket_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RocketViewHolder, position: Int) {
        val rocket = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(rocket.flickr_images?.get(0)).into(ivRocketImage)
            tvTitle.text = rocket.name
        }
    }
}