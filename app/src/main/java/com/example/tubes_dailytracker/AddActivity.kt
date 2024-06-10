package com.example.tubes_dailytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        val etTaskName = findViewById<EditText>(R.id.et_taskname)
        val etNotesTask = findViewById<EditText>(R.id.et_notes_task)
        val btnTaskSchedule = findViewById<Button>(R.id.btn_taskschedule)

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val btnAddTask = findViewById<Button>(R.id.btn_addhabit)
        btnAddTask.setOnClickListener {
            val taskName = etTaskName.text.toString()
            val taskNotes = etNotesTask.text.toString()
            val taskSchedule = btnTaskSchedule.text.toString()

            val userID = auth.currentUser?.uid ?: "default"

            val task = hashMapOf(
                "task_name" to taskName,
                "task_notes" to taskNotes,
                "task_duedate" to taskSchedule,
                "userID" to userID
            )

            db.collection("task")
                .add(task)
                .addOnSuccessListener { documentReference ->
                    Log.d("AddTaskActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }
}