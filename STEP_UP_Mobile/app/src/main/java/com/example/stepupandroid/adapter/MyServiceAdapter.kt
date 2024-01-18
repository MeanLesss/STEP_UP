package com.example.stepupandroid.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stepupandroid.R
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.model.response.MyServiceItem

class MyServiceAdapter(private val context: Context, private val itemList: List<MyServiceItem>) :
    RecyclerView.Adapter<MyServiceAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_service, parent, false)
        return ItemViewHolder(itemView)
    }

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
        holder.descriptionTextView.text = currentItem.description
        holder.serviceType.text = currentItem.service_type
        holder.startDateTextView.text =
            Util.convertDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd-MMM-yyyy", currentItem.start_date)
        holder.endDateTextView.text =
            Util.convertDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd-MMM-yyyy", currentItem.end_date)

        // Set the background drawable with the border color
        val backgroundDrawable =
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_drawable)

        // Cast the drawable to a GradientDrawable (assuming your border drawable is a GradientDrawable)
        if (backgroundDrawable is GradientDrawable) {
            // Set the stroke color
            when (currentItem.stringStatus) {
                "Expired/Declined" -> {
                    backgroundDrawable.setStroke(
                        5,
                        ContextCompat.getColor(context, R.color.status_declined)
                    )
                    holder.statusText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.status_declined
                        )
                    )
                    holder.statusIcon.setImageResource(R.drawable.icon_failed)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_declined)
                    )
                }

                "Pending" -> {
                    backgroundDrawable.setStroke(
                        5,
                        ContextCompat.getColor(context, R.color.status_pending)
                    )
                    holder.statusText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.status_pending
                        )
                    )
                    holder.statusIcon.setImageResource(R.drawable.icon_in_progress)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_pending)
                    )
                }

                "Active" -> {
                    backgroundDrawable.setStroke(
                        5,
                        ContextCompat.getColor(context, R.color.status_active)
                    )
                    holder.statusText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.status_active
                        )
                    )
                    holder.statusIcon.setImageResource(R.drawable.icon_success)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_active)
                    )
                }

                "Cancel" -> {
                    backgroundDrawable.setStroke(
                        5,
                        ContextCompat.getColor(context, R.color.status_fail)
                    )
                    holder.statusText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.status_fail
                        )
                    )
                    holder.statusIcon.setImageResource(R.drawable.icon_failed)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_fail)
                    )
                }

                else -> {
                    backgroundDrawable.setStroke(
                        5,
                        ContextCompat.getColor(context, R.color.status_unknown)
                    )
                    holder.statusText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.status_unknown
                        )
                    )
                    holder.statusIcon.setImageResource(R.drawable.icon_unknown)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_unknown)
                    )
                }
            }
            holder.statusText.text = currentItem.stringStatus
            backgroundDrawable.setColor(ContextCompat.getColor(context, R.color.selected_color))
            // Apply the modified drawable to the containerLayout's background
            holder.containerLayout.background = backgroundDrawable
        }

        // Handle the click event for the "View" button here if needed
        holder.viewButton.setOnClickListener {
            // Handle the click event for the "View" button
            Log.d("bug test", currentItem.title + " clicked")
        }
    }

    override fun getItemCount() = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val statusText: TextView = itemView.findViewById(R.id.statusText)
        val statusIcon: ImageView = itemView.findViewById(R.id.statusIcon)
        val image: ImageView = itemView.findViewById(R.id.image)
        val serviceType: TextView = itemView.findViewById(R.id.serviceType)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val startDateTextView: TextView = itemView.findViewById(R.id.startDate)
        val endDateTextView: TextView = itemView.findViewById(R.id.endDate)
        val viewButton: LinearLayout = itemView.findViewById(R.id.viewButton)
        val containerLayout: LinearLayout = itemView.findViewById(R.id.containerLayout)
    }
}