package com.example.tubes_dailytracker

import MyListAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListAdapter
import android.widget.ListView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val listView = findViewById<ListView>(R.id.listView)
        val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        val adapter = MyListAdapter(this, R.layout.list_item, items)
        listView.adapter = adapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.nav_habit -> {
                    // Respond to navigation item 2 click
                    val intent = Intent(this, HabitActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_add -> {
                    // Respond to navigation item 3 click
                    val intent = Intent(this, AddActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_calendar -> {
                    // Respond to navigation item 3 click
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Respond to navigation item 3 click
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
}