package com.isu.covidvolunteer.ui.order

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.isu.covidvolunteer.models.order.AddOrderDto
import com.isu.covidvolunteer.models.order.Address
import com.isu.covidvolunteer.models.order.OrderDto

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