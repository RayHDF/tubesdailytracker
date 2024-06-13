package com.example.tubes_dailytracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val etEmail = view.findViewById<EditText>(R.id.et_email_profile)
        val etPhoneNumber = view.findViewById<EditText>(R.id.et_phonenumber)
        val etFirstName = view.findViewById<EditText>(R.id.et_firstname)
        val etLastName = view.findViewById<EditText>(R.id.et_lastname)
        val btnUpdateProfile = view.findViewById<Button>(R.id.btnUpdateProfile)

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid ?: "default"

        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("test", "DocumentSnapshot data: ${document.data}")

                    val email = document.getString("email")
                    val phoneNumber = document.getString("number")
                    val firstName = document.getString("first_name")
                    val lastName = document.getString("last_name")
                    val name = document.getString("name")

                    if (firstName.isNullOrEmpty() && lastName.isNullOrEmpty()) {
                        etFirstName?.setText(name)
                    } else {
                        etFirstName?.setText(firstName)
                        etLastName?.setText(lastName)
                    }

                    etEmail.setText(email)
                    etPhoneNumber.setText(phoneNumber)
                } else {
                    Log.d("test", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("test", "get failed with ", exception)
            }

        btnUpdateProfile.setOnClickListener {
            val updatedEmail = etEmail.text.toString()
            val updatedPhoneNumber = etPhoneNumber.text.toString()
            val updatedFirstName = etFirstName.text.toString()
            val updatedLastName = etLastName.text.toString()

            val userRef = db.collection("users").document(userId)

            userRef
                .update(
                    mapOf(
                        "email" to updatedEmail,
                        "number" to updatedPhoneNumber,
                        "name" to updatedFirstName,
                        "last_name" to updatedLastName
                    )
                )
                .addOnSuccessListener {
                    Log.d("test", "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e ->
                    Log.w("test", "Error updating document", e)
                }
        }


        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}