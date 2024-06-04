package com.example.tubes_dailytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.hbb20.CountryCodePicker

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = Firebase.auth


        val nameInput = findViewById<EditText>(R.id.et_name)
        val emailInput = findViewById<EditText>(R.id.et_email)
        val numberInput = findViewById<EditText>(R.id.et_phonenumber)
        val passwordInput = findViewById<EditText>(R.id.et_password)
        val signupButton = findViewById<Button>(R.id.btn_signupconfirm)

        val cPicker = findViewById<CountryCodePicker>(R.id.pick_flag)
        cPicker.setDefaultCountryUsingNameCode("ID")
        cPicker.resetToDefaultCountry()

        signupButton.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val number = numberInput.text.toString()
            val password = passwordInput.text.toString()

            if (name.isEmpty() || email.isEmpty() || number.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill every fields!", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            val userId = firebaseUser?.uid

                            if (userId != null) {
                                val user = hashMapOf(
                                    "name" to name,
                                    "email" to email,
                                    "number" to number
                                )

                                db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener {
                                        val intent = Intent(this, SignupSuccessActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to Store Data", Toast.LENGTH_SHORT).show()
                                    }

                            } else {
                                Toast.makeText(this, "User ID is Null", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Sign Up Failed! Email already exists",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}