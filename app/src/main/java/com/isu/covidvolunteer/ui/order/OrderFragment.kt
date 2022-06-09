package com.isu.covidvolunteer.ui.order

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.retrofit.CustomResponse

class OrderFragment : Fragment(R.layout.fragment_order_user) {
    private lateinit var orderViewModel: OrderViewModel

    // Views
    private lateinit var orderHeadlineView: TextView
    private lateinit var orderOwnerView: TextView
    private lateinit var orderAddressFromView: TextView
    private lateinit var orderAddressToView: TextView
    private lateinit var orderDateView: TextView
    private lateinit var orderCommentView: TextView
    private lateinit var respondButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderViewModel = ViewModelProvider(
            this,
            OrderViewModelFactory(OrderRepository())
        )[OrderViewModel::class.java]

        orderHeadlineView = view.findViewById(R.id.orderUserHeadlineTextView)
        orderOwnerView = view.findViewById(R.id.orderUserOwnerTextView)
        orderAddressFromView = view.findViewById(R.id.orderUserAddressFromTextView)
        orderAddressToView = view.findViewById(R.id.orderUserAddressToTextView)
        orderDateView = view.findViewById(R.id.orderUserDateTextView)
        orderCommentView = view.findViewById(R.id.orderUserCommentTextView)
        respondButton = view.findViewById(R.id.orderActionButton)
        respondButton.setOnClickListener {
            orderViewModel.respondToOrder(arguments?.get("id") as Long)
            Thread.sleep(250)
            findNavController().popBackStack()
        }

        orderViewModel.getOrder(arguments?.get("id") as Long)
        orderViewModel.order.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CustomResponse.Success -> {
                    orderHeadlineView.text = "${it.body.headline}"
                    orderOwnerView.text = "Медицинский работник: ${it.body.owner.firstName} ${it.body.owner.lastName}"

                    orderAddressFromView.text = "Откуда: ${it.body.address?.from}"
                    if (!it.body.address?.from.isNullOrBlank()) {
                        orderAddressFromView.setOnClickListener {
                            findNavController().navigate(
                                R.id.action_orderFragment_to_userMapsFragment,
                                bundleOf("address" to orderAddressFromView.text.toString())
                            )
                        }
                        orderAddressFromView.setTextColor(Color.BLUE)
                    }

                    orderAddressToView.text = "Куда: ${it.body.address?.to}"
                    if (!it.body.address?.to.isNullOrBlank()) {
                        orderAddressToView.setOnClickListener {
                            findNavController().navigate(
                                R.id.action_orderFragment_to_userMapsFragment,
                                bundleOf("address" to orderAddressToView.text.toString())
                            )
                        }
                        orderAddressToView.setTextColor(Color.BLUE)
                    }

                    orderDateView.text = "Дата ${it.body.date}"
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
    }
}