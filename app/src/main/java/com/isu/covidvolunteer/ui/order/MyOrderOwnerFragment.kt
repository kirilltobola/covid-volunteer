package com.isu.covidvolunteer.ui.order

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.ChatRepository
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.ui.chat.ChatViewModel
import com.isu.covidvolunteer.ui.chat.ChatViewModelFactory

class MyOrderOwnerFragment : Fragment(R.layout.my_order_owner) {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var chatViewModel: ChatViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderViewModel = ViewModelProvider(
            this,
            OrderViewModelFactory(OrderRepository())
        )[OrderViewModel::class.java]

        chatViewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(ChatRepository())
        )[ChatViewModel::class.java]

        orderViewModel.getOrder(arguments?.get("id") as Long)
        orderViewModel.order.observe(viewLifecycleOwner, Observer {
            view.findViewById<TextView>(R.id.myOrderOwnerTextView).text = it.toString()
        })

        // Start chat button
        view.findViewById<Button>(R.id.myOrderOwnerStartChatButton).setOnClickListener {
            var ownerId = arguments?.get("ownerId") as Long
            Log.d("MY_TAG", ownerId.toString())
            chatViewModel.startChat(ownerId)
        }

        // Done button
        view.findViewById<Button>(R.id.myOrderDoneButton).setOnClickListener {
            // TODO: implement this
        }

        // Edit button
        view.findViewById<Button>(R.id.myOrderOwnerEditButton).setOnClickListener {
            // TODO: implement this
        }
    }
}