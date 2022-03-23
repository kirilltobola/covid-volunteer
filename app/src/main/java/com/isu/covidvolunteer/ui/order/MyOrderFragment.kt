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
import com.isu.covidvolunteer.repository.UserRepository
import com.isu.covidvolunteer.ui.chat.ChatViewModel
import com.isu.covidvolunteer.ui.chat.ChatViewModelFactory

class MyOrderFragment : Fragment(R.layout.fragment_my_order_user) {
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
            view.findViewById<TextView>(R.id.myOrderUserHeadlineTextView).text = "${it.headline}"
            view.findViewById<TextView>(R.id.myOrderUserOwnerTextView).text = "Медицинский работник: ${it.owner.firstName} ${it.owner.lastName}"
            view.findViewById<TextView>(R.id.myOrderUserAddressFromTextView).text = "Откуда: ${it.address?.from}"
            view.findViewById<TextView>(R.id.myOrderUserAddressToTextView).text = "Куда: ${it.address?.to}"
            view.findViewById<TextView>(R.id.myOrderUserCommentTextView).text =  "Комментарий: ${it.comment}"
        })

        // Start chat button
        view.findViewById<Button>(R.id.myOrderStartChatButton).setOnClickListener {
            var ownerId = arguments?.get("ownerId") as Long
            Log.d("MY_TAG", ownerId.toString())
            chatViewModel.startChat(ownerId)
        }

        // Decline button
        view.findViewById<Button>(R.id.myOrderDeclineButton).setOnClickListener {
            orderViewModel.decline(arguments?.get("id") as Long)
        }
    }
}