package com.example.tubes_dailytracker

import MyListAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tubes_dailytracker.databinding.ActivityHomeBinding
import android.widget.ListAdapter
import android.widget.ListView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var currentFragmentId: Int = R.id.navigation_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    currentFragmentId = R.id.navigation_home
                    true
                }
                R.id.navigation_habit -> {
                    loadFragment(HabitFragment())
                    currentFragmentId = R.id.navigation_habit
                    true
                }
                R.id.navigation_calendar -> {
                    loadFragment(CalendarFragment())
                    currentFragmentId = R.id.navigation_calendar
                    true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.navigation_add -> {
                    handleAddNav()
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_home
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun handleAddNav() {
        when (currentFragmentId){
            R.id.navigation_home -> {
                val intent = Intent(this, AddDailyActivity::class.java)
                startActivity(intent)
            }
            R.id.navigation_habit -> {
                val intent = Intent(this, AddHabitActivity::class.java)
                startActivity(intent)
            }
            R.id.navigation_calendar -> {
                val intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
