package com.isu.covidvolunteer.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.ChatRepository

class ChatFragment : Fragment(R.layout.fragment_chat) {

    lateinit var chatViewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        chatViewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(ChatRepository())
        )[ChatViewModel::class.java]

        var view = inflater.inflate(R.layout.fragment_chat, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.chatRecyclerView)

        var layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, true)
        //layoutManager.
        //layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager //LinearLayoutManager(view.context)

        chatViewModel.getChat(arguments?.get("chatId") as Long)
        chatViewModel.liveData.observe(viewLifecycleOwner, Observer {
            var adapter = ChatRecyclerAdapter(it.asReversed()) // TODO: reversed by kotlin, maybe in do it in db
            recyclerView.adapter = adapter
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.sendMessageButton).setOnClickListener {
            val message = view.findViewById<EditText>(R.id.messageEditText).text
            chatViewModel.sendMessage(arguments?.get("chatId") as Long, message.toString())
            view.findViewById<EditText>(R.id.messageEditText).text.clear()

            chatViewModel.getChat(arguments?.get("chatId") as Long)
        }
    }
}