package com.example.tubes_dailytracker

import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment

class AddActivity : AppCompatActivity(), DatePickerFragment.DatePickerListener {

    private lateinit var btn_date : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val btn_back = findViewById<Button>(R.id.btn_back)
        val et_taskname = findViewById<EditText>(R.id.et_taskname)
        val et_taksHabit = findViewById<EditText>(R.id.et_notes_task)

        btn_date = findViewById(R.id.btn_taskschedule)

        btn_date.setOnClickListener {
            showDatePickerDialog()
        }

        btn_back.setOnClickListener {
            finish()
        }

    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDateSet(year: Int, month: Int, day: Int) {
        processDatePickerResult(year, month, day)
    }

    private fun processDatePickerResult(year: Int, month: Int, day: Int) {
        val monthString = (month + 1).toString()
        val dayString = day.toString()
        val yearString = year.toString()

        val dateMessage = "$monthString/$dayString/$yearString"
        Toast.makeText(this, "Date: $dateMessage", Toast.LENGTH_SHORT).show()

        btn_date.text = dateMessage
    }
}