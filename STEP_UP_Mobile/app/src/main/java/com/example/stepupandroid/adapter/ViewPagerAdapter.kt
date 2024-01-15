package com.example.stepupandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stepupandroid.R

class ViewPagerAdapter(
    private val images: MutableList<String>,
    private val onImageClick: (Int) -> Unit
) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_viewpager, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = images[position]
        // Load image using Glide or another image loading library
        Glide.with(holder.imageView.context)
            .load(imageUrl)
            .error(R.drawable.step_up_logo)
            .into(holder.imageView)

        holder.imageView.setOnClickListener {
            onImageClick(position)

        }
    }

    override fun getItemCount(): Int = images.size
}