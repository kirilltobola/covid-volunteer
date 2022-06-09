package com.isu.covidvolunteer.ui.order

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.ChatRepository
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.ui.chat.ChatViewModel
import com.isu.covidvolunteer.ui.chat.ChatViewModelFactory

class MyOrderFragment : Fragment(R.layout.fragment_my_order_user) {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var chatViewModel: ChatViewModel

    // Views
    private lateinit var orderHeadlineView: TextView
    private lateinit var orderOwnerView: TextView
    private lateinit var orderAddressFromView: TextView
    private lateinit var orderAddressToView: TextView
    private lateinit var orderCommentView: TextView
    private lateinit var startButton: ImageButton
    private lateinit var declineButton: ImageButton

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

        orderHeadlineView = view.findViewById(R.id.myOrderUserHeadlineTextView)
        orderOwnerView = view.findViewById(R.id.myOrderUserOwnerTextView)
        orderAddressFromView = view.findViewById(R.id.myOrderUserAddressFromTextView)
        orderAddressToView = view.findViewById(R.id.myOrderUserAddressToTextView)
        orderCommentView = view.findViewById(R.id.myOrderUserCommentTextView)

        orderViewModel.getOrder(arguments?.get("id") as Long)
        orderViewModel.order.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CustomResponse.Success -> {
                    if (!orderOwnerView.text.isNullOrBlank()) {
                        orderOwnerView.setOnClickListener { _ ->
                            findNavController().navigate(
                                R.id.action_myOrderFragment_to_profileFragment,
                                bundleOf("user" to it.body.owner)
                            )
                        }
                        orderOwnerView.setTextColor(Color.BLUE)
                    }

                    orderHeadlineView.text = "${it.body.headline}"
                    orderOwnerView.text = "Медицинский работник: ${it.body.owner.firstName} ${it.body.owner.lastName}"

                    orderAddressFromView.text = "Откуда: ${it.body.address?.from}"
                    if (!it.body.address?.from.isNullOrBlank()) {
                        orderAddressFromView.setOnClickListener {
                            findNavController().navigate(
                                R.id.action_myOrderFragment_to_userMapsFragment,
                                bundleOf("address" to orderAddressFromView.text.toString())
                            )
                        }
                        orderAddressFromView.setTextColor(Color.BLUE)
                    }

                    orderAddressToView.text = "Куда: ${it.body.address?.to}"
                    if (!it.body.address?.to.isNullOrBlank()) {
                        orderAddressToView.setOnClickListener {
                            findNavController().navigate(
                                R.id.action_myOrderFragment_to_userMapsFragment,
                                bundleOf("address" to orderAddressToView.text.toString())
                            )
                        }
                        orderAddressToView.setTextColor(Color.BLUE)
                    }

                    orderCommentView.text =  "Комментарий: ${it.body.comment}"
                }
                is CustomResponse.NetworkError -> {
                    val msg = "Отсутсвтует интернет соединение"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
                is CustomResponse.ApiError -> {
                    val msg = it.body.message
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
                is CustomResponse.UnexpectedError -> {
                    val msg = "Неизвестная ошибка"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        })

        startButton = view.findViewById(R.id.myOrderStartChatButton)
        startButton.setOnClickListener {
            val ownerId = arguments?.get("ownerId") as Long
            chatViewModel.startChat(ownerId)
            chatViewModel.chat.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is CustomResponse.Success -> {
                        findNavController().navigate(
                            R.id.action_myOrderFragment_to_chatFragment,
                            bundleOf(
                                "chatId" to it.body.id,
                                "userId" to "${it.body.user.firstName} ${it.body.user.lastName}"
                            )
                        )
                    }
                    is CustomResponse.NetworkError -> {
                        val msg = "Отсутсвтует интернет соединение"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                    is CustomResponse.ApiError -> {
                        val msg = it.body.message
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                    is CustomResponse.UnexpectedError -> {
                        val msg = "Неизвестная ошибка"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        declineButton = view.findViewById(R.id.myOrderDeclineButton)
        declineButton.setOnClickListener {
            orderViewModel.decline(arguments?.get("id") as Long)
            Thread.sleep(250)
            findNavController().popBackStack()
        }
    }
}