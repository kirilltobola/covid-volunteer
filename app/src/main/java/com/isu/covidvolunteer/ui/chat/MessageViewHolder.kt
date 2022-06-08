package com.isu.covidvolunteer.ui.chat

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.models.message.MessageDto

abstract class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    open lateinit var textView: TextView

    abstract fun bind(message: MessageDto)
}