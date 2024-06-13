package com.example.tubes_dailytracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
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

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val listView: ListView = view.findViewById(R.id.listView)
        val data = mutableListOf<String>()

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val userID = auth.currentUser?.uid ?: "default"
        db.collection("task")
            .whereEqualTo("userID", userID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val item = document.getString("task_name")
                    if (item != null) {
                        data.add(item)
                    }
                }
                val adapter = CustomAdapter(requireContext(), R.layout.list_item, data)
                listView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("HomeFragment", "Error getting documents: ", exception)
            }


        val calendar = view.findViewById<CalendarView>(R.id.cv_tasks)

        calendar.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            val date = "${month + 1}/$dayOfMonth/$year"

            db.collection("task")
                .whereEqualTo("userID", userID)
                .whereEqualTo("task_duedate", date)
                .get()
                .addOnSuccessListener { documents ->
                    data.clear() // clear the old data
                    for (document in documents) {
                        val item = document.getString("task_name") // replace "item" with the actual field name
                        if (item != null) {
                            data.add(item)
                        }
                    }
                    Log.w("CalendarFragment", "Date: $date")
                    val adapter = CustomAdapter(requireContext(), R.layout.list_item, data)
                    listView.adapter = adapter
                }
                .addOnFailureListener { exception ->
                    Log.w("CalendarFragment", "Error getting documents: ", exception)
                }

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
         * @return A new instance of fragment CalendarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}