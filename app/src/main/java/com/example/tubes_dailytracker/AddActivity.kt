package com.example.tubes_dailytracker

import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val button: Button = findViewById(R.id.btn_taskschedule)
        button.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    fun processDatePickerResult(year: Int, month: Int, day: Int) {
        // The month parameter is zero-based, so add 1
        val monthString = (month + 1).toString()
        val dayString = day.toString()
        val yearString = year.toString()

        // Display the chosen date in a toast message
        val dateMessage = "$monthString/$dayString/$yearString"
        Toast.makeText(this, "Date: $dateMessage", Toast.LENGTH_SHORT).show()
    }

}