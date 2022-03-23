package com.isu.covidvolunteer.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.ChatRepository
import com.isu.covidvolunteer.ui.OnItemClickListener

class ChatsFragment : Fragment(R.layout.fragment_chats) {
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

        var view = inflater.inflate(R.layout.fragment_chats, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.chatsRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        chatViewModel.getChats()
        chatViewModel.chats.observe(viewLifecycleOwner, Observer {
            var adapter = ChatsRecyclerAdapter(it)

            adapter.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Log.d("MY_TAG", it[position].id.toString())
                    findNavController().navigate(
                        R.id.action_chatsFragment_to_chatFragment,
                        bundleOf("chatId" to it[position].id)
                    )
                }
            })

            recyclerView.adapter = adapter
        })
        return view
        //val recyclerView: RecyclerView = view.findViewById(R.id.chatsRecyclerView)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.adapter = ChatsRecyclerAdapter(fillList())
    }
}