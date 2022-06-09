package com.isu.covidvolunteer.ui.chat

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.ChatRepository
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.ui.OnItemClickListener

class ChatsFragment : Fragment(R.layout.fragment_chats) {
    lateinit var chatViewModel: ChatViewModel

    //Views
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chatViewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(ChatRepository())
        )[ChatViewModel::class.java]

        recyclerView = view.findViewById(R.id.chatsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        chatViewModel.getChats()
        chatViewModel.chats.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CustomResponse.Success -> {
                    if (it.body.isNotEmpty()) {
                        view.findViewById<TextView>(R.id.chatsEmptyTextView).isVisible = false

                        val adapter = ChatsRecyclerAdapter(it.body.asReversed())

                        adapter.setOnItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                findNavController().navigate(
                                    R.id.action_chatsFragment_to_chatFragment,
                                    bundleOf(
                                        "chatId" to it.body[position].id,
                                        "userId" to "${it.body[position].user.firstName} ${it.body[position].user.lastName}"
                                    )
                                )
                            }
                        })
                        recyclerView.adapter = adapter
                    }

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