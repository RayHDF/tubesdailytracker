package com.example.tubes_dailytracker

import android.os.Bundle
import android.util.Log
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddHabitActivity : AppCompatActivity() {

    private lateinit var tbDailyHabit: ToggleButton
    private lateinit var tbWeeklyHabit: ToggleButton
    private lateinit var tbMonthlyHabit: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_habit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val et_habitName = findViewById<EditText>(R.id.et_habitname)
        val et_notesHabit = findViewById<EditText>(R.id.et_notes_habit)
        val btn_back = findViewById<View>(R.id.btn_back)

        tbDailyHabit = findViewById(R.id.tb_daily_habit)
        tbWeeklyHabit = findViewById(R.id.tb_weekly_habit)
        tbMonthlyHabit = findViewById(R.id.tb_monthly_habit)

        tbDailyHabit.setOnClickListener(toggleButtonClickListener)
        tbWeeklyHabit.setOnClickListener(toggleButtonClickListener)
        tbMonthlyHabit.setOnClickListener(toggleButtonClickListener)



        btn_back.setOnClickListener {
            finish()
        }

        val etHabitName = findViewById<EditText>(R.id.et_habitname)
        val etNotesHabit = findViewById<EditText>(R.id.et_notes_habit)
        val tbDaily = findViewById<ToggleButton>(R.id.tb_daily_habit)
        val tbWeekly = findViewById<ToggleButton>(R.id.tb_weekly_habit)
        val tbMonthly = findViewById<ToggleButton>(R.id.tb_monthly_habit)

        val btnAddHabit = findViewById<Button>(R.id.btn_addhabit)

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()


        btnAddHabit.setOnClickListener {
            val habitName = etHabitName.text.toString()
            val habitNotes = etNotesHabit.text.toString()
            val habitType = when {
                tbDaily.isChecked -> "Daily"
                tbWeekly.isChecked -> "Weekly"
                tbMonthly.isChecked -> "Monthly"
                else -> ""
            }

            val userID = auth.currentUser?.uid ?: "default"

            if (habitName.isEmpty() || habitType.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val habit = hashMapOf(
                    "habit_name" to habitName,
                    "habit_notes" to habitNotes,
                    "reset_counter" to habitType,
                    "userID" to userID
                    )

                db.collection("habit")
                    .add(habit)
                    .addOnSuccessListener { documentReference ->
                        Log.d("AddHabitActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
                        Toast.makeText(this, "Habit added", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.w("AddHabitActivity", "Error adding document", e)
                    }
            }

            // Send to database

        }



    }

    private val toggleButtonClickListener = View.OnClickListener { v ->
        tbDailyHabit.isChecked = false
        tbWeeklyHabit.isChecked = false
        tbMonthlyHabit.isChecked = false

        val clickedButton = v as ToggleButton
        clickedButton.isChecked = true
    }
}
