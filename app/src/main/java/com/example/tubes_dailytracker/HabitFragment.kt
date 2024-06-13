package com.example.tubes_dailytracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HabitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HabitFragment : Fragment() {
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

    fun checkIn(userId: String, tvCheckin: TextView) {
        val db = FirebaseFirestore.getInstance()
        val currentDate = Calendar.getInstance().time

        db.collection("daily_checkin").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val lastCheckinDate = document.getDate("last_checkin_date")
                    val calendar1 = Calendar.getInstance()
                    val calendar2 = Calendar.getInstance()
                    calendar1.time = currentDate
                    calendar2.time = lastCheckinDate

                    // Check if the user is checking in on a new day
                    if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR) ||
                        calendar1.get(Calendar.DAY_OF_YEAR) != calendar2.get(Calendar.DAY_OF_YEAR)) {
                        val checkinCount = document.getLong("checkin_count") ?: 0
                        document.reference.update("checkin_count", checkinCount + 1, "last_checkin_date", currentDate)
                        tvCheckin.text = "${checkinCount + 1} Days"
                    } else {
                        tvCheckin.text = "${document.getLong("checkin_count")} Days"
                    }
                } else {
                    // Create a new document for the user
                    val data = hashMapOf(
                        "user_id" to userId,
                        "checkin_count" to 1L,
                        "last_checkin_date" to currentDate
                    )
                    db.collection("daily_checkin").document(userId).set(data)
                    tvCheckin.text = "1 Day"
                }
            }
            .addOnFailureListener { exception ->
                Log.w("CheckIn", "Error checking in user: ", exception)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_habit, container, false)

        val listView: ListView = view.findViewById(R.id.listView)
        val tvCheckIn: TextView = view.findViewById(R.id.tv_checkin)

        val db = FirebaseFirestore.getInstance()

        val data = mutableListOf<String>()

        val auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser?.uid ?: "default"

        db.collection("habit")
            .whereEqualTo("userID", userID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val habit = document.getString("habit_name") // replace "habit_name" with the actual field name

                    if (habit != null) {
                        data.add(habit)
                    }
                }
                val adapter = HabitAdapter(requireContext(), R.layout.list_item_habit, data)
                listView.adapter = adapter

                checkIn(userID, tvCheckIn)
            }
            .addOnFailureListener { exception ->
                Log.w("Habit", "Error getting documents: ", exception)
            }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HabitFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HabitFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}