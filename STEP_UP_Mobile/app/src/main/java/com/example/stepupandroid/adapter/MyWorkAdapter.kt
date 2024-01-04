package com.example.stepupandroid.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stepupandroid.R
import com.example.stepupandroid.model.MyWork

class MyWorkAdapter(private val context: Context, private val itemList: List<MyWork>) :
    RecyclerView.Adapter<MyWorkAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_work_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description
        holder.nameTextView.text = currentItem.name
        holder.startDateTextView.text = currentItem.startDate
        holder.endDateTextView.text = currentItem.endDate

        // Set the background drawable with the border color
        val backgroundDrawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_drawable)

        // Cast the drawable to a GradientDrawable (assuming your border drawable is a GradientDrawable)
        if (backgroundDrawable is GradientDrawable) {
            // Set the stroke color
            when(currentItem.status){
                "completed" -> {
                    backgroundDrawable.setStroke(5, ContextCompat.getColor(context, R.color.status_completed))
                    holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.status_completed))
                    holder.statusIcon.setImageResource(R.drawable.icon_success)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_completed)
                    )
                }
                "canceled" -> {
                    backgroundDrawable.setStroke(5, ContextCompat.getColor(context, R.color.status_canceled))
                    holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.status_canceled))
                    holder.statusIcon.setImageResource(R.drawable.icon_failed)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_canceled)
                    )
                }
                "in progress" -> {
                    backgroundDrawable.setStroke(5, ContextCompat.getColor(context, R.color.status_in_progress))
                    holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.status_in_progress))
                    holder.statusIcon.setImageResource(R.drawable.icon_in_progress)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_in_progress)
                    )
                }
            }
            holder.statusText.text = currentItem.status
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
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val startDateTextView: TextView = itemView.findViewById(R.id.startDate)
        val endDateTextView: TextView = itemView.findViewById(R.id.endDate)
        val viewButton: LinearLayout = itemView.findViewById(R.id.viewButton)
        val containerLayout: LinearLayout = itemView.findViewById(R.id.containerLayout)
    }
}