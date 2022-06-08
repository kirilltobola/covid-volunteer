package com.isu.covidvolunteer.ui.order

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.order.AddOrderDto
import com.isu.covidvolunteer.models.order.Address
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.repository.OrderRepository
import kotlinx.android.synthetic.main.fragment_add_order.*

class AddOrderFragment : EditOrderFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editButton.text = "Добавить"
    }

    override fun makeRequest(order: OrderDto?) {
        val headline = orderHeadline.text.toString()
        val from = orderAddressFrom.text.toString()
        val to = orderAddressTo.text.toString()
        val date = orderDate.text.toString()
        val time = orderTime.text.toString()
        val comment = orderComment.text.toString()

        val errorMsg = "Обязательное поле"
        if (headline.isNullOrBlank()) {
            orderHeadline.error = "Обязательное поле"
        } else if (date.isNullOrBlank() || time.isNullOrBlank()) {
            orderDate.error = errorMsg
            orderTime.error = errorMsg
        } else {
            orderViewModel.addOrder(AddOrderDto(
                headline,
                Address(from, to),
                "$date $time",
                comment
            ))
            findNavController().popBackStack()
        }
    }
}
//    lateinit var orderViewModel: OrderViewModel
//
//    // Views
//    private lateinit var headlineView: EditText
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        orderViewModel = ViewModelProvider(
//            this,
//            OrderViewModelFactory(OrderRepository())
//        )[OrderViewModel::class.java]
//
//        headlineView = view.findViewById(R.id.headlineEditText)
//
//        view.findViewById<Button>(R.id.createOrderButton).setOnClickListener {
//            val headline = headlineView.text.toString()
//            val from = view.findViewById<EditText>(R.id.addressFromEditText).text.toString()
//            val to = view.findViewById<EditText>(R.id.addressToEditText).text.toString()
//            val date = view.findViewById<EditText>(R.id.orderDateEditText).text.toString()
//            val comment = view.findViewById<EditText>(R.id.orderCommentEditText).text.toString()
//
//            if (headline.isNullOrBlank()) {
//                headlineView.error = "Обязательное поле"
//            } else {
//                orderViewModel.addOrder(AddOrderDto(
//                    headline,
//                    Address(from, to),
//                    date,
//                    comment
//                ))
//                findNavController().popBackStack()
//            }
//        }
//    }
//}