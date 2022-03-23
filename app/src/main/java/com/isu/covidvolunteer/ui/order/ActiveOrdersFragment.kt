package com.isu.covidvolunteer.ui.order

import android.os.Bundle
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
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.repository.UserRepository
import com.isu.covidvolunteer.ui.OnItemClickListener
import com.isu.covidvolunteer.util.UserDetails

class ActiveOrdersFragment : Fragment(R.layout.fragment_active_orders) {
    lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        orderViewModel = ViewModelProvider(
            this,
            OrderViewModelFactory(OrderRepository())
        )[OrderViewModel::class.java]

        var view = inflater.inflate(R.layout.fragment_active_orders, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.activeOrdersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        orderViewModel.getActiveOrders()
        orderViewModel.activeOrders.observe(viewLifecycleOwner, Observer {
            var adapter = OrdersAdapter(it)

            adapter.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val destination = when {
                        it[position].owner.id == UserDetails.id -> {
                            R.id.action_activeOrdersFragment_to_myOrderOwnerFragment
                        }
                        it[position].performer?.id == UserDetails.id -> {
                            R.id.action_activeOrdersFragment_to_myOrderFragment
                        }
                        else -> {
                            R.id.action_activeOrdersFragment_to_orderFragment
                        }
                    }
                    findNavController().navigate(
                        destination,
                        bundleOf("id" to it[position].id)
                    )
                }
            })

            recyclerView.adapter = adapter
        })

        return view
    }
}