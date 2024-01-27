package com.example.stepupandroid.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stepupandroid.R
import com.example.stepupandroid.model.response.ServiceItem

class ServiceAdapter(
    private val context: Context,
    private val itemList: MutableList<ServiceItem>,
    private val listener: OnServiceSelected
) :
    RecyclerView.Adapter<ServiceAdapter.ItemViewHolder>() {

    interface OnServiceSelected {
        fun onServiceSelected(serviceId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ItemViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        if (!currentItem.attachments.isNullOrEmpty()) {
            val firstImageUrl =
                currentItem.attachments.values.firstOrNull() // Get the value of the first map entry
            Glide.with(context)
                .load(firstImageUrl)
                .error(R.drawable.step_up_logo)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.step_up_logo)
        }

        holder.titleTextView.text = currentItem.title
        holder.description.text = currentItem.description
        holder.viewCount.text = currentItem.view.toString()
        holder.rating.rating = currentItem.service_rate.toFloat()
        holder.serviceType.text = currentItem.service_type
        holder.price.text = "$" + currentItem.price

        val backgroundDrawable =
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_drawable)
        if (backgroundDrawable is GradientDrawable) {
            backgroundDrawable.setStroke(5, ContextCompat.getColor(context, R.color.primary_color))
            backgroundDrawable.setColor(ContextCompat.getColor(context, R.color.selected_color))
            // Apply the modified drawable to the containerLayout's background
            holder.containerLayout.background = backgroundDrawable
        }

        if (currentItem.discount == 0f) {
            holder.discountLayout.visibility = View.GONE
        } else {
            holder.discountLayout.visibility = View.VISIBLE
            holder.discountPercentage.text = "-" + String.format("%.2f", currentItem.discount) + "%"
        }

        holder.containerLayout.setOnClickListener {
            listener.onServiceSelected(currentItem.id)
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
        val discountLayout: FrameLayout = itemView.findViewById(R.id.discountLayout)
        val discountPercentage: TextView = itemView.findViewById(R.id.discountPercentage)
    }

    // Method to add new items to the existing list
    @SuppressLint("NotifyDataSetChanged")
    fun addData(newData: List<ServiceItem>) {
        itemList.addAll(newData)
        notifyDataSetChanged()
    }
}
