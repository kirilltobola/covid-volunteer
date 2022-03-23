package com.isu.covidvolunteer.ui.order

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.repository.UserRepository

class OrderFragment : Fragment(R.layout.fragment_order_user) {
    private lateinit var orderViewModel: OrderViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderViewModel = ViewModelProvider(
            this,
            OrderViewModelFactory(OrderRepository())
        )[OrderViewModel::class.java]

        orderViewModel.getOrder(arguments?.get("id") as Long)
        orderViewModel.order.observe(viewLifecycleOwner, Observer {
            view.findViewById<TextView>(R.id.orderUserHeadlineTextView).text = "${it.headline}"
            view.findViewById<TextView>(R.id.orderUserOwnerTextView).text = "Медицинский работник: ${it.owner.firstName} ${it.owner.lastName}"
            view.findViewById<TextView>(R.id.orderUserAddressFromTextView).text = "Откуда: ${it.address?.from}"
            view.findViewById<TextView>(R.id.orderUserAddressToTextView).text = "Куда: ${it.address?.to}"
            view.findViewById<TextView>(R.id.orderUserCommentTextView).text =  "Комментарий: ${it.comment}"
        })

        // TODO: here if user is owner
        view.findViewById<Button>(R.id.orderActionButton).setOnClickListener {
            orderViewModel.respondToOrder(arguments?.get("id") as Long)
        }
    }
}