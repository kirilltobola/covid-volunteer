package com.isu.covidvolunteer.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.UserRepository
import com.isu.covidvolunteer.ui.OnItemClickListener
import com.isu.covidvolunteer.ui.user.UserViewModel
import com.isu.covidvolunteer.ui.user.UserViewModelFactory
import com.isu.covidvolunteer.util.UserDetails

class MyOrdersMedicFragment : Fragment(R.layout.fragment_my_orders_medic) {
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

        var view = inflater.inflate(R.layout.fragment_my_orders_medic, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.myOrdersMedicRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        userViewModel.getMyOrders(UserDetails.id!!)
        userViewModel.userOrders.observe(viewLifecycleOwner, Observer {
            var adapter = OrdersAdapter(it)

            adapter.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val destination = if (it[position].owner.id == UserDetails.id) {
                        R.id.action_myOrdersMedicFragment_to_myOrderOwnerFragment
                    } else {
                        R.id.action_myOrdersMedicFragment_to_myOrderFragment
                    }
                    findNavController().navigate(
                        destination,
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.addOrderButton).setOnClickListener {
            findNavController().navigate(R.id.action_myOrdersMedicFragment_to_addOrderFragment)
        }
    }
}