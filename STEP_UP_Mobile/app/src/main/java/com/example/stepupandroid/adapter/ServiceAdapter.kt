package com.example.stepupandroid.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stepupandroid.R
import com.example.stepupandroid.model.response.ServiceItem

class ServiceAdapter(private val context: Context, private val itemList: MutableList<ServiceItem>) :
    RecyclerView.Adapter<ServiceAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.service_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        if (!currentItem.attachments.isNullOrEmpty()) {
            val firstImageUrl = currentItem.attachments.values.firstOrNull() // Get the value of the first map entry
            Glide.with(context)
                .load(firstImageUrl)
                .error(R.drawable.no_image)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.no_image)
        }

        holder.titleTextView.text = currentItem.title
        holder.description.text = currentItem.description
        holder.viewCount.text = currentItem.view.toString()
        holder.rating.rating = currentItem.service_rate.toFloat()
        holder.serviceType.text = currentItem.service_type
        holder.price.text = currentItem.price

        // Handle the click event for the "View" button here if needed
        holder.priceButton.setOnClickListener {
            // Handle the click event for the "View" button
            // You can perform any action here
        }
        val backgroundDrawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_drawable)
        if (backgroundDrawable is GradientDrawable) {
            backgroundDrawable.setStroke(5, ContextCompat.getColor(context, R.color.primary_color))
            backgroundDrawable.setColor(ContextCompat.getColor(context, R.color.selected_color))
            // Apply the modified drawable to the containerLayout's background
            holder.containerLayout.background = backgroundDrawable
        }

    }

    override fun getItemCount() = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val image: ImageView = itemView.findViewById(R.id.image)
        val description: TextView = itemView.findViewById(R.id.description)
        val viewCount: TextView = itemView.findViewById(R.id.viewCount)
        val rating: RatingBar = itemView.findViewById(R.id.rating)
        val serviceType: TextView = itemView.findViewById(R.id.serviceType)
        val priceButton: LinearLayout = itemView.findViewById(R.id.priceButton)
        val price: TextView = itemView.findViewById(R.id.price)
        val containerLayout: LinearLayout = itemView.findViewById(R.id.containerLayout)
    }

    // Method to add new items to the existing list
    @SuppressLint("NotifyDataSetChanged")
    fun addData(newData: List<ServiceItem>) {
        itemList.addAll(newData)
        notifyDataSetChanged()
    }
}
