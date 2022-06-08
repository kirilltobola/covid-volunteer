package com.isu.covidvolunteer.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.message.MessageDto
import com.isu.covidvolunteer.util.UserDetails

class ChatRecyclerAdapter(
    private val messages: List<MessageDto>
    ) : RecyclerView.Adapter<MessageViewHolder>() {
    //private val SENT_VIEW_TYPE = 0
    //private val RECEIVED_VIEW_TYPE = 1

    class ReceivedMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override var textView: TextView = itemView.findViewById(R.id.text_gchat_message_other)
        var time: TextView = itemView.findViewById(R.id.messageReceivedTime)

        override fun bind(message: MessageDto) {
            textView.text = message.body
            time.text = message.created
        }
    }

    class SentMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override var textView: TextView = itemView.findViewById(R.id.text_gchat_message_me)
        var time: TextView = itemView.findViewById(R.id.messageSentTime)

        override fun bind(message: MessageDto) {
            textView.text = message.body
            time.text = message.created
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        if (viewType == SENT_VIEW_TYPE) {
            return SentMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_chat_me,
                    parent,
                    false
                )
            )
        } else {
            return ReceivedMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_chat,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender.id == UserDetails.id) {
            SENT_VIEW_TYPE
        } else RECEIVED_VIEW_TYPE
    }

    companion object {
        const val SENT_VIEW_TYPE = 0
        const val RECEIVED_VIEW_TYPE = 1
    }
}