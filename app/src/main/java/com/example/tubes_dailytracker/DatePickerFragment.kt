package com.example.tubes_dailytracker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface DatePickerListener {
        fun onDateSet(year: Int, month: Int, day: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireActivity(), R.style.MyDatePickerDialogTheme, this, year, month, day)

        datePickerDialog.datePicker.minDate = c.timeInMillis

        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, 1)

        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val activity = activity as? DatePickerListener
        activity?.onDateSet(year, month, day)
    }
}
