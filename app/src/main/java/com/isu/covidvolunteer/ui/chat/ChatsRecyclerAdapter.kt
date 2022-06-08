package com.isu.covidvolunteer.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.chat.ChatDto
import com.isu.covidvolunteer.ui.OnItemClickListener

class ChatsRecyclerAdapter(
    private val chats: List<ChatDto>
    ) : RecyclerView.Adapter<ChatsRecyclerAdapter.ChatsViewHolder>() {
    private lateinit var _clickListener: OnItemClickListener

    class ChatsViewHolder(
        itemView: View, clickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.textView1)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        _clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val itemView = LayoutInflater.from(
            parent.context
        ).inflate(R.layout.recyclerview_item, parent, false)
        return ChatsViewHolder(itemView, _clickListener)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.textView1.text = "${chats[position].user.firstName} ${chats[position].user.lastName}"
    }

    override fun getItemCount(): Int = chats.size
}