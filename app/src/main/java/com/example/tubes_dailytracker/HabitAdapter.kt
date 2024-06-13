package com.example.tubes_dailytracker

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class HabitAdapter(context: Context, private val resource: Int, private var items: MutableList<String>)
    : ArrayAdapter<String>(context, resource, items) {

    private val checkBoxStates = MutableList(items.size) { false }
    private val db = FirebaseFirestore.getInstance()
    private val habitCounters = mutableMapOf<String, Long>()

    init {
        // Fetch all habits and their testCounter values
        db.collection("habit")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val habitName = document.getString("habit_name")
                    val testCounter = document.getLong("testCounter") ?: 0
                    if (habitName != null) {
                        habitCounters[habitName] = testCounter
                    }
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("HabitAdapter", "Error getting documents: ", exception)
            }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val textView: TextView = view.findViewById(R.id.textView)
        val habitCounterTextView: TextView = view.findViewById(R.id.habitCounterTextView)

        val item = getItem(position)
        textView.text = item

        checkBox.isChecked = checkBoxStates[position]

        // Set the testCounter value from the map
        val habitCounter = if (item != null) habitCounters[item]?.toString() else "0"
        habitCounterTextView.text = habitCounter

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            Log.d("HabitAdapter", "Checkbox checked: $isChecked")
            if (isChecked) {
                db.collection("habit")
                    .whereEqualTo("habit_name", item)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val currentCounter = document.getLong("testCounter") ?: 0
                            val newCounter = currentCounter + 1
                            db.collection("habit").document(document.id)
                                .update("testCounter", newCounter)
                                .addOnSuccessListener {
                                    Log.d("HabitAdapter", "DocumentSnapshot successfully updated!")
                                    habitCounterTextView.text = newCounter.toString()
                                    item?.let { habitCounters[it] = newCounter }
                                }
                                .addOnFailureListener { e ->
                                    Log.w("HabitAdapter", "Error updating document", e)
                                }
                        }
                    }
            }
        }

        return view
    }
}