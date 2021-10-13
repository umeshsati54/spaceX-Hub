package com.usati.spacex.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.usati.spacex.R
import com.usati.spacex.models.launch.Launch
import kotlinx.android.synthetic.main.rocket_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class LaunchAdapter : RecyclerView.Adapter<LaunchAdapter.LaunchViewHolder>() {
    inner class LaunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Launch>() {
        override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        return LaunchViewHolder(
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

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        val launch = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(launch.links?.patch?.small)
                .placeholder(R.drawable.rocket_launch_outline).into(ivRocketImage)
            tvTitle.text = launch.name
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm ")
            val date: Date = inputFormat.parse(launch.date_utc)
            val formattedDate: String = outputFormat.format(date)

            tvSubTitle.text = formattedDate + "IST"
            setOnClickListener {
                onItemClickListener?.let {
                    it(launch)
                }
            }
        }
    }

    private var onItemClickListener: ((Launch) -> Unit)? = null

    fun setOnItemClickListener(listener: (Launch) -> Unit) {
        onItemClickListener = listener
    }
}