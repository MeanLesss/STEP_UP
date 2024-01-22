package com.example.stepupandroid.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stepupandroid.R
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.model.response.MyOrder

class MyOrderAdapter(
    private val context: Context,
    private val itemList: List<MyOrder>,
    private val listener: OnOrderSelected
) :
    RecyclerView.Adapter<MyOrderAdapter.ItemViewHolder>() {

    interface OnOrderSelected {
        fun onOrderSelected(orderId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_work, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.titleTextView.text = currentItem.order_title
        holder.descriptionTextView.text = currentItem.order_description
        holder.nameTextView.text = currentItem.freelancer_id.toString()
        holder.startDateTextView.text = Util.convertDate(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "dd-MMM-yyyy",
            currentItem.expected_start_date
        )
        holder.endDateTextView.text = Util.convertDate(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "dd-MMM-yyyy",
            currentItem.expected_end_date
        )

        // Set the background drawable with the border color
        val backgroundDrawable =
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_drawable)

        // Cast the drawable to a GradientDrawable (assuming your border drawable is a GradientDrawable)
        if (backgroundDrawable is GradientDrawable) {
            // Set the stroke color
            when (currentItem.stringStatus) {
                Constants.Declined -> {
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

                Constants.Pending -> {
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

                Constants.InProgress -> {
                    backgroundDrawable.setStroke(
                        5,
                        ContextCompat.getColor(context, R.color.status_in_progress)
                    )
                    holder.statusText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.status_in_progress
                        )
                    )
                    holder.statusIcon.setImageResource(R.drawable.icon_in_progress)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_in_progress)
                    )
                }

                Constants.InReview -> {
                    backgroundDrawable.setStroke(
                        5,
                        ContextCompat.getColor(context, R.color.status_in_review)
                    )
                    holder.statusText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.status_in_review
                        )
                    )
                    holder.statusIcon.setImageResource(R.drawable.icon_in_progress)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_in_review)
                    )
                }

                Constants.Success -> {
                    backgroundDrawable.setStroke(
                        5,
                        ContextCompat.getColor(context, R.color.status_success)
                    )
                    holder.statusText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.status_success
                        )
                    )
                    holder.statusIcon.setImageResource(R.drawable.icon_success)
                    holder.statusIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_success)
                    )
                }

                Constants.Fail -> {
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

        holder.containerLayout.setOnClickListener {
            listener.onOrderSelected(currentItem.id)
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
        val containerLayout: LinearLayout = itemView.findViewById(R.id.containerLayout)
    }
}