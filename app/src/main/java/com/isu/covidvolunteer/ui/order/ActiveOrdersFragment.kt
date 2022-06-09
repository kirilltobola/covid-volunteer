package com.isu.covidvolunteer.ui.order

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
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.ui.OnItemClickListener
import com.isu.covidvolunteer.util.UserDetails

class ActiveOrdersFragment : Fragment(R.layout.fragment_active_orders) {
    lateinit var orderViewModel: OrderViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderViewModel = ViewModelProvider(
            this,
            OrderViewModelFactory(OrderRepository())
        )[OrderViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.activeOrdersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        orderViewModel.getActiveOrders()
        orderViewModel.activeOrders.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CustomResponse.Success -> {
                    if (it.body.isNotEmpty()) {
                        view.findViewById<TextView>(R.id.activeOrdersEmptyTextView).isVisible = false

                        val adapter = OrdersAdapter(it.body.asReversed())
                        adapter.setOnItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                val reversedPos = it.body.size - 1 - position
                                val destination = when {
                                    it.body[reversedPos].owner.id == UserDetails.id -> {
                                        R.id.action_activeOrdersFragment_to_myOrderOwnerFragment
                                    }
                                    it.body[reversedPos].performer?.id == UserDetails.id -> {
                                        R.id.action_activeOrdersFragment_to_myOrderFragment
                                    }
                                    else -> {
                                        R.id.action_activeOrdersFragment_to_orderFragment
                                    }
                                }
                                findNavController().navigate(
                                    destination,
                                    bundleOf("id" to it.body[reversedPos].id)
                                )
                            }
                        })
                        recyclerView.adapter = adapter
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