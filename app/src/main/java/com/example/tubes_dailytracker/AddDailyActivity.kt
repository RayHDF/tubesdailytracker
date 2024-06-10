package com.example.tubes_dailytracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddDailyActivity : AppCompatActivity(), DatePickerFragment.DatePickerListener {
    private lateinit var btn_date: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_daily)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerItems = arrayOf("Daily", "Weekly", "Monthly")

        val spinner: Spinner = findViewById(R.id.spin_repeat)

        val adapter = ArrayAdapter(
            this,
            R.layout.custom_spinner_layout,
            spinnerItems
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@AddDailyActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(0)
            }
        }

        btn_date = findViewById(R.id.btn_dailyschedule)

        btn_date.setOnClickListener {
            showDatePickerDialog()
        }

        val btnBack = findViewById<Button>(R.id.btn_back)

        btnBack.setOnClickListener {
            finish()
        }

        val btnAddDaily = findViewById<Button>(R.id.btn_addhabit)
        val etTaskName = findViewById<EditText>(R.id.et_taskname)
        val etNotesTask = findViewById<EditText>(R.id.et_notes_task)
        val btnDailySchedule = findViewById<Button>(R.id.btn_dailyschedule)
        val spinRepeat = findViewById<Spinner>(R.id.spin_repeat)
        val etEvery = findViewById<EditText>(R.id.et_every)

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        btnAddDaily.setOnClickListener {
            val taskName = etTaskName.text.toString()
            val taskNotes = etNotesTask.text.toString()
            val taskSchedule = btnDailySchedule.text.toString()
            val repeat = spinRepeat.selectedItem.toString()
            val every = etEvery.text.toString()


            val userID = auth.currentUser?.uid ?: "default"

            val daily = hashMapOf(
                "task_name" to taskName,
                "task_notes" to taskNotes,
                "task_schedule" to taskSchedule,
                "repeat" to repeat,
                "every" to every,
                "userID" to userID
            )

            db.collection("daily")
                .add(daily)
                .addOnSuccessListener { documentReference ->
                    Log.d("AddDailyActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(this, "Daily Task added", Toast.LENGTH_SHORT).show()
                    finish()
                }


            Log.w(
                "AddDailyActivity",
                "Task Name: $taskName, Task Notes: $taskNotes, Task Schedule: $taskSchedule, Repeat: $repeat, Every: $every")
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

