package com.isu.covidvolunteer.ui.chat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.message.MessageDto
import com.isu.covidvolunteer.util.UserDetails

class ChatRecyclerAdapter(val messages: List<MessageDto>) : RecyclerView.Adapter<ChatRecyclerAdapter.ChatViewHolder>() {
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.messageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.chat_message,
                parent,
                false
            )
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.textView.text = messages[position].body
//        if(messages[position].sender.id == UserDetails.id) {
//            holder.textView.setTextColor(Color.parseColor("#00FF00")) // TODO: changing message color
//        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}