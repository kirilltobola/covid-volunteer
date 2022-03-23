package com.isu.covidvolunteer.ui.order

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
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.repository.UserRepository
import com.isu.covidvolunteer.ui.OnItemClickListener
import com.isu.covidvolunteer.ui.user.UserViewModel
import com.isu.covidvolunteer.ui.user.UserViewModelFactory

class MyOrdersFragment : Fragment(R.layout.fragment_my_orders) {
    lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserRepository())
        )[UserViewModel::class.java]

        var view = inflater.inflate(R.layout.fragment_my_orders, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.myOrdersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        userViewModel.getMyOrders(7) // TODO: create user model with token id and roles
        userViewModel.userOrders.observe(viewLifecycleOwner, Observer {
            var adapter = OrdersAdapter(it)

            adapter.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    findNavController().navigate(
                        R.id.action_myOrdersFragment_to_myOrderFragment,
                        bundleOf(
                            "id" to it[position].id,
                            "ownerId" to it[position].owner.id
                        )
                    )
                }
            })
            recyclerView.adapter = adapter
        })
        return view
    }

}