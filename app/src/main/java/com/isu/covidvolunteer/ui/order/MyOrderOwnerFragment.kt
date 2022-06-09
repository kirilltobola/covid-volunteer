package com.isu.covidvolunteer.ui.order

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.models.order.Status
import com.isu.covidvolunteer.repository.ChatRepository
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.ui.chat.ChatViewModel
import com.isu.covidvolunteer.ui.chat.ChatViewModelFactory

class MyOrderOwnerFragment : Fragment(R.layout.my_order_owner) {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var chatViewModel: ChatViewModel

    // Views
    private lateinit var orderHeadlineField: TextView
    private lateinit var ownerField: TextView
    private lateinit var performerField: TextView
    private lateinit var orderStatusView: TextView
    private lateinit var orderDateView: TextView
    private lateinit var orderAddressViewFrom: TextView
    private lateinit var orderAddressViewTo: TextView
    private lateinit var orderCommentView: TextView
    private lateinit var uniteButton: ImageButton
    private lateinit var startChatButton: ImageButton
    private lateinit var doneButton: ImageButton
    private lateinit var editButton: ImageButton

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

        performerField = view.findViewById(R.id.myOrderOwnerPerformer)
        ownerField = view.findViewById(R.id.myOrderOwnerOwner)
        orderStatusView = view.findViewById(R.id.myOrderOwnerStatus)
        orderDateView = view.findViewById(R.id.myOrderOwnerDate)
        orderAddressViewFrom = view.findViewById(R.id.myOrderOwnerAddressFrom)
        orderAddressViewTo = view.findViewById(R.id.myOrderOwnerAddressTo)
        orderCommentView = view.findViewById(R.id.myOrderOwnerComment)
        orderHeadlineField = view.findViewById(R.id.myOrderOwnerHeadline)

        orderViewModel.getOrder(arguments?.get("id") as Long)

        orderViewModel.order.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CustomResponse.Success -> {
                    if (it.body.performer != null) {
                        performerField.setOnClickListener { _ ->
                            findNavController().navigate(
                                R.id.action_myOrderOwnerFragment_to_profileFragment,
                                bundleOf("user" to it.body.performer)
                            )
                        }
                        performerField.setTextColor(Color.BLUE)
                    }

                    orderHeadlineField.text = it.body.headline
                    ownerField.text = "Медик: ${it.body.owner}"
                    if (it.body.performer != null) {
                        performerField.text = "Волонтер: ${it.body.performer}"
                    } else {
                        performerField.text = "Волонтер: нет"
                    }


                    val status = when (it.body.status) {
                        Status.ACTIVE -> "Свободный"
                        Status.IN_PROGRESS -> "Выполняется"
                        Status.DONE -> "Выполнен"
                    }
                    orderStatusView.text = "Статус: $status"
                    orderDateView.text = "Дата: ${it.body.date.toString()}"
                    orderAddressViewFrom.text = "Откуда: ${it.body.address?.from}"
                    orderAddressViewTo.text = "Куда: ${it.body.address?.to}"
                    orderCommentView.text = "Комментарий: ${it.body.comment}"


                    uniteButton = view.findViewById(R.id.myOrderOwnerUntieButton)
                    if (it.body.performer == null) {
                        uniteButton.isEnabled = false
                    }
                    uniteButton.setOnClickListener {
                        val orderId = arguments?.get("id") as Long
                        orderViewModel.untie(orderId)
                        findNavController().popBackStack()
                    }

                    startChatButton = view.findViewById(R.id.myOrderOwnerStartChatButton)
                    if (it.body.performer == null) {
                        startChatButton.isEnabled = false
                    }
                    startChatButton.setOnClickListener {
                        val performerId = arguments?.get("performerId")
                        if (performerId != null) {
                            chatViewModel.startChat(performerId as Long)
                            chatViewModel.chat.observe(
                                viewLifecycleOwner,
                                Observer { chatResponse ->
                                    when (chatResponse) {
                                        is CustomResponse.Success -> {
                                            findNavController().navigate(
                                                R.id.action_myOrderOwnerFragment_to_chatFragment,
                                                bundleOf(
                                                    "chatId" to chatResponse.body.id,
                                                    "userId" to "${chatResponse.body.user.firstName} ${chatResponse.body.user.lastName}"
                                                )
                                            )
                                        }
                                        is CustomResponse.NetworkError -> {
                                            val msg = "Отсутсвтует интернет соединение"
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        }
                                        is CustomResponse.ApiError -> {
                                            val msg = chatResponse.body.message
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        }
                                        is CustomResponse.UnexpectedError -> {
                                            val msg = "Неизвестная ошибка"
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }
                    }

                    doneButton = view.findViewById(R.id.myOrderDoneButton)
                    doneButton.setOnClickListener {
                        val orderId = arguments?.get("id") as Long
                        orderViewModel.done(orderId)
                        doneButton.isEnabled = false
                        findNavController().popBackStack()
                    }

                    editButton = view.findViewById(R.id.myOrderOwnerEditButton)
                    editButton.setOnClickListener { _ ->
                        val ord: OrderDto = it.body
                        findNavController().navigate(
                            R.id.action_myOrderOwnerFragment_to_editOrderFragment,
                            bundleOf("order" to ord)
                        )
                    }
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
}