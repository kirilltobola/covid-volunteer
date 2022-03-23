package com.isu.covidvolunteer.ui.order

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.order.AddOrderDto
import com.isu.covidvolunteer.models.order.Address
import com.isu.covidvolunteer.models.order.Status
import com.isu.covidvolunteer.repository.OrderRepository

class AddOrderFragment : Fragment(R.layout.fragment_add_order) {
    lateinit var orderViewModel: OrderViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        orderViewModel = ViewModelProvider(
            this,
            OrderViewModelFactory(OrderRepository())
        )[OrderViewModel::class.java]

        view.findViewById<Button>(R.id.createOrderButton).setOnClickListener {
            val headline = view.findViewById<EditText>(R.id.headlineEditText).text.toString()
            val from = view.findViewById<EditText>(R.id.addressFromEditText).text.toString()
            val to = view.findViewById<EditText>(R.id.addressToEditText).text.toString()
            val date = view.findViewById<EditText>(R.id.orderDateEditText).text.toString()
            val comment = view.findViewById<EditText>(R.id.orderCommentEditText).text.toString()

            orderViewModel.addOrder(AddOrderDto(
                headline,
                Address(from, to),
                date,
                comment
            ))
        }
    }
}