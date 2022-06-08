package com.isu.covidvolunteer.ui.order

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(requireActivity(), this, hour, minute, true)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
         calendar.set(Calendar.HOUR_OF_DAY, hour)
         calendar.set(Calendar.MINUTE, minute)

        val time = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.time)
        setFragmentResult(
            "REQUEST_KEY",
            bundleOf("SELECTED_TIME" to time)
        )
    }
}