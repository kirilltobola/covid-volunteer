package com.isu.covidvolunteer.ui.chat

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.ChatRepository
import com.isu.covidvolunteer.retrofit.CustomResponse

class ChatFragment : Fragment(R.layout.fragment_chat) {
    lateinit var chatViewModel: ChatViewModel

    // Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatNameView: TextView
    private lateinit var messageFieldView: EditText
    private lateinit var sendMessageButton: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chatViewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(ChatRepository())
        )[ChatViewModel::class.java]

        recyclerView = view.findViewById(R.id.chatRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            true
        )

        chatNameView = view.findViewById(R.id.chatName)
        chatNameView.text = arguments?.get("userId").toString()

        messageFieldView = view.findViewById(R.id.messageEditText)

        sendMessageButton = view.findViewById(R.id.sendMessageButton)
        sendMessageButton.setOnClickListener {
            if (!messageFieldView.text.isNullOrBlank()) {
                val message = messageFieldView.text.toString()
                val chatId = arguments?.get("chatId") as Long
                chatViewModel.sendMessage(chatId, message)
                messageFieldView.text.clear()
                chatViewModel.getChatMessages(chatId)
            }
        }

        chatViewModel.getChatMessages(arguments?.get("chatId") as Long)
        chatViewModel.liveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CustomResponse.Success -> {
                    val adapter = ChatRecyclerAdapter(it.body.asReversed())
                    recyclerView.adapter = adapter
                }
                is CustomResponse.ApiError -> {
                    val msg = it.body.message
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
                is CustomResponse.NetworkError -> {
                    val msg = "Отсутствует интернет соединение"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
                is CustomResponse.UnexpectedError -> {
                    val msg = "Неизвестная ошибка"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}