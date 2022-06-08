package com.isu.covidvolunteer.ui.order


import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.order.Address
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.repository.OrderRepository
import java.util.*

open class EditOrderFragment : Fragment(R.layout.fagment_edit_order) {
    protected lateinit var orderViewModel: OrderViewModel

    // Views:
    protected lateinit var orderHeadline: EditText
    protected lateinit var orderAddressFrom: TextView
    protected lateinit var orderAddressTo: TextView
    protected lateinit var orderDate: TextView
    protected lateinit var orderTime: TextView
    protected lateinit var orderComment: TextView
    protected lateinit var editButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init view model TODO: use delegating?
        orderViewModel = ViewModelProvider(
            this,
            OrderViewModelFactory(OrderRepository())
        )[OrderViewModel::class.java]

        // get Order bundle
        val order = arguments?.get("order") as OrderDto?

        // Init views
        orderHeadline = view.findViewById(R.id.fragmentEditOrderHeadlineEditText)
        orderAddressFrom = view.findViewById(R.id.fragmentEditOrderAddressFromEditText)
        orderAddressTo = view.findViewById(R.id.fragmentEditOrderAddressToEditText)
        orderDate = view.findViewById(R.id.fragmentEditOrderOrderDateEditText)
        orderTime = view.findViewById(R.id.fragmentEditOrderOrderTimeEditText)
        orderComment = view.findViewById(R.id.fragmentEditOrderOrderCommentEditText)
        editButton = view.findViewById(R.id.fragmentEditOrderEditOrderButton)

        orderHeadline.setText(order?.headline)

        orderAddressFrom.setTextColor(Color.BLUE)
        if (!order?.address?.from.isNullOrBlank()) {
            orderAddressFrom.text = order?.address?.from
        }
        orderAddressFrom.setOnClickListener {
            val supportFragmentManager = requireActivity().supportFragmentManager
            val mapsDialogFragment = MapsFragment()
            if (orderAddressFrom.text != null) {
                mapsDialogFragment.arguments = bundleOf("addressLine" to orderAddressFrom.text)
            }

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner) {
                    resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                        val address = bundle.get("SELECTED_ADDRESS")
                        orderAddressFrom.text = address.toString()
                }
            }
            mapsDialogFragment.show(supportFragmentManager, "MapsDialogFragment")
        }

        orderAddressTo.setTextColor(Color.BLUE)
        if (!order?.address?.to.isNullOrBlank()) {
            orderAddressTo.text = order?.address?.to
        }
        orderAddressTo.setOnClickListener {
            val mapsDialogFragment = MapsFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager
            if (orderAddressTo.text != null) {
                mapsDialogFragment.arguments = bundleOf("addressLine" to orderAddressTo.text)
            }

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner) {
                    resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val address = bundle.get("SELECTED_ADDRESS")
                    Log.d("DIALOG_MAPS", address.toString())
                    orderAddressTo.text = address.toString()
                }
            }
            mapsDialogFragment.show(supportFragmentManager, "MapsDialogFragment")
        }

        val datetime = order?.date?.split(" ")
        val date = datetime?.get(0)
        val time = datetime?.get(1)
        orderDate.text = date
        orderDate.setTextColor(Color.BLUE)
        orderDate.setOnClickListener {
            Log.d("DATE_PICKER", "touched!")
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner) {
                resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.get("SELECTED_DATE")
                    Log.d("DATE_PICKER", date.toString())
                    orderDate.text = date.toString()
                }
            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }

        orderTime.text = time
        orderTime.setTextColor(Color.BLUE)
        orderTime.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner) {
                resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val time = bundle.get("SELECTED_TIME")
                    orderTime.text = time.toString()
                }
            }
            timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
        }

        orderComment.text = order?.comment

        // Submit button
        editButton.setOnClickListener {
            makeRequest(order)
//            val geocoder = Geocoder(context, Locale("RU"))
//            if (!orderAddressFrom.text.toString().isNullOrBlank()) {
//                val loc = geocoder.getFromLocationName(orderAddressFrom.text.toString(), 1)
//            }
//
//
//            val headline = orderHeadline.text.toString()
//            val from = orderAddressFrom.text.toString()//loc[0].getAddressLine(0)
//            val to = orderAddressTo.text.toString()
//            val date = orderDate.text.toString()
//            val time = orderTime.text.toString()
//            val comment = orderComment.text.toString()
//
//            if (headline.isNullOrBlank()) {
//                orderHeadline.error = "Заполните заголовок"
//            } else {
//                order?.headline = headline
//                order?.address = Address(from, to)
//                order?.date = "$date $time"
//                order?.comment = comment
//
//                if (order != null) {
//                    orderViewModel.edit(order)
//                    Log.d("EDIT_ORDER", "EDITED")
//
//                    findNavController().popBackStack(R.id.editOrderFragment, true)
//                }
//            }
        }
    }

    open fun makeRequest(order: OrderDto?) {
        val geocoder = Geocoder(context, Locale("RU"))
        if (!orderAddressFrom.text.toString().isNullOrBlank()) {
            val loc = geocoder.getFromLocationName(orderAddressFrom.text.toString(), 1)
        }


        val headline = orderHeadline.text.toString()
        val from = orderAddressFrom.text.toString()//loc[0].getAddressLine(0)
        val to = orderAddressTo.text.toString()
        val date = orderDate.text.toString()
        val time = orderTime.text.toString()
        val comment = orderComment.text.toString()


        val errorMsg = "Обязательное поле"
        if (headline.isNullOrBlank()) {
            orderHeadline.error = errorMsg
        } else if (date.isNullOrBlank() || time.isNullOrBlank()) {
            orderDate.error = errorMsg
            orderTime.error = errorMsg
        } else {
            order?.headline = headline
            order?.address = Address(from, to)
            order?.date = "$date $time"
            order?.comment = comment

            if (order != null) {
                orderViewModel.edit(order)
                Log.d("EDIT_ORDER", "EDITED")

                findNavController().popBackStack(R.id.editOrderFragment, true)
            }
        }
    }
}