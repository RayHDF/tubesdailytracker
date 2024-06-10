package com.example.tubes_dailytracker

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tubes_dailytracker.schema.HabitSchema
import com.example.tubes_dailytracker.schema.ProfileSchema
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Get data from firebase table 'users' in database
        val db = Firebase.firestore
        val userRef = db.collection("users")
        userRef.whereEqualTo("name", "asd").get().addOnSuccessListener { result ->
            for (document in result) {
                val user = document.toObject(ProfileSchema::class.java)
                Log.d("User", "ID: ${document.id}, Name: ${user.name}, Email: ${user.email}, Number: ${user.number}")
            }
        }.addOnFailureListener { exception ->
            Log.w("User", "Error getting documents: ", exception)
        }
    }
}