package com.example.stepupandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stepupandroid.R
import com.example.stepupandroid.model.Attachment

class AttachmentAdapter(private val attachments: MutableList<Attachment>) :
    RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textAttachmentName: TextView = view.findViewById(R.id.attachmentName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attachment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attachment = attachments[position]
        holder.textAttachmentName.text = attachment.fileName
    }

    override fun getItemCount() = attachments.size

    fun addAttachment(attachment: Attachment) {
        attachments.add(attachment)
        notifyItemInserted(attachments.size - 1)
    }
}