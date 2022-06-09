package com.isu.covidvolunteer.ui.order

import android.os.Bundle
import android.view.View
import android.widget.Button
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
import com.isu.covidvolunteer.repository.UserRepository
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.ui.OnItemClickListener
import com.isu.covidvolunteer.ui.user.UserViewModel
import com.isu.covidvolunteer.ui.user.UserViewModelFactory
import com.isu.covidvolunteer.util.UserDetails

class MyOrdersMedicFragment : Fragment(R.layout.fragment_my_orders_medic) {
    lateinit var userViewModel: UserViewModel

    // Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var addOrderButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserRepository())
        )[UserViewModel::class.java]

        addOrderButton = view.findViewById(R.id.addOrderButton)
        addOrderButton.setOnClickListener {
            findNavController().navigate(R.id.action_myOrdersMedicFragment_to_addOrderFragment)
        }

        recyclerView = view.findViewById(R.id.myOrdersMedicRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        userViewModel.getMyOrders(UserDetails.id!!)
        userViewModel.userOrders.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CustomResponse.Success -> {
                    if (it.body.isNotEmpty()) {
                        view.findViewById<TextView>(R.id.myOrdersMedicEmptyTextView).isVisible = false

                        val adapter = OrdersAdapter(it.body.asReversed())

                        adapter.setOnItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                val destination = if (it.body[position].owner.id == UserDetails.id) {
                                    R.id.action_myOrdersMedicFragment_to_myOrderOwnerFragment
                                } else {
                                    R.id.action_myOrdersMedicFragment_to_myOrderFragment
                                }
                                findNavController().navigate(
                                    destination,
                                    bundleOf(
                                        "id" to it.body[position].id,
                                        "ownerId" to it.body[position].owner.id,
                                        "performerId" to it.body[position].performer?.id
                                    )
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