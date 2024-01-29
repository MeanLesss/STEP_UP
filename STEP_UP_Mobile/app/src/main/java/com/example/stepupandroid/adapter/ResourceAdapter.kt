package com.example.stepupandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stepupandroid.R
import com.example.stepupandroid.model.Attachment
import com.example.stepupandroid.model.DownloadAttachment

class ResourceAdapter(private val resources: MutableList<DownloadAttachment>) :
    RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {

    private var listener: OnDownloadClick? = null
    interface OnDownloadClick {
        fun onDownloadClick(attachment: DownloadAttachment)
    }

    fun setOnDownloadClick(listener: OnDownloadClick) {
        this.listener = listener
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textAttachmentName: TextView = view.findViewById(R.id.attachmentName)
        val downloadButton: ImageView = view.findViewById(R.id.download)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resource, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resource = resources[position]
        holder.textAttachmentName.text = resource.fileName
        holder.downloadButton.setOnClickListener {
            listener?.onDownloadClick(resource)
        }
    }

    override fun getItemCount() = resources.size
}