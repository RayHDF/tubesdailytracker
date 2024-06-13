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

class CustomCalendarAdapter(context: Context, private val resource: Int, private var items: MutableList<String>)
    : ArrayAdapter<String>(context, resource, items) {

    private val db = FirebaseFirestore.getInstance()
    private val checkBoxStates = MutableList(items.size) { false }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val textView: TextView = view.findViewById(R.id.textView)

        val item = getItem(position)
        textView.text = item

        checkBox.isChecked = checkBoxStates[position]

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                db.collection("task")
                    .whereEqualTo("task_name", item)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("task").document(document.id).delete()
                                .addOnSuccessListener {
                                    Log.d("CustomAdapter", "DocumentSnapshot successfully deleted!")

                                    items.removeAt(position)
                                    notifyDataSetChanged()
                                }
                                .addOnFailureListener { e ->
                                    Log.w("CustomAdapter", "Error deleting document", e)
                                }

                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.w("CustomAdapter", "Error getting documents: ", exception)
                    }

                buttonView.background = context.getDrawable(R.drawable.checkbox_checked)
            } else {
                buttonView.background = context.getDrawable(R.drawable.checkbox_background)
            }
        }

        return view
    }
}