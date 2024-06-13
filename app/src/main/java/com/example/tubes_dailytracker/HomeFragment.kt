package com.example.tubes_dailytracker

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)  // This is important to enable menu options in the fragment

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val userID = auth.currentUser?.uid ?: "default"

        db.collection("users").document(userID)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name = document.getString("name")
                    val tvNameHome = view?.findViewById<TextView>(R.id.tv_name_home)
                    tvNameHome?.text = name
                } else {
                    Log.d("HomeFragment", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("HomeFragment", "get failed with ", exception)
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val listView: ListView = view.findViewById(R.id.listView)
        val data = mutableListOf<String>()

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val userID = auth.currentUser?.uid ?: "default"

        db.collection("daily")
            .whereEqualTo("userID", userID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val item = document.getString("task_name") // replace "item" with the actual field name
                    if (item != null) {
                        data.add(item)
                    }
                }
                val adapter = CustomHomeAdapter(requireContext(), R.layout.list_item, data)
                listView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("HomeFragment", "Error getting documents: ", exception)
            }

        return view
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
