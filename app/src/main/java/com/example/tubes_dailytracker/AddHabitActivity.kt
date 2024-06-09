package com.example.tubes_dailytracker

import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View

class AddHabitActivity : AppCompatActivity() {

    private lateinit var tbDailyHabit: ToggleButton
    private lateinit var tbWeeklyHabit: ToggleButton
    private lateinit var tbMonthlyHabit: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_habit) // Correct layout file
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tbDailyHabit = findViewById(R.id.tb_daily_habit)
        tbWeeklyHabit = findViewById(R.id.tb_weekly_habit)
        tbMonthlyHabit = findViewById(R.id.tb_monthly_habit)

        tbDailyHabit.setOnClickListener(toggleButtonClickListener)
        tbWeeklyHabit.setOnClickListener(toggleButtonClickListener)
        tbMonthlyHabit.setOnClickListener(toggleButtonClickListener)
    }

    private val toggleButtonClickListener = View.OnClickListener { v ->
        tbDailyHabit.isChecked = true
        tbWeeklyHabit.isChecked = false
        tbMonthlyHabit.isChecked = false

        val clickedButton = v as ToggleButton
        clickedButton.isChecked = true
    }
}
