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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class CustomHomeAdapter(context: Context, private val resource: Int, private var items: MutableList<String>, private val homeFragment: HomeFragment)
    : ArrayAdapter<String>(context, resource, items) {
    private val checkBoxStates = MutableList(items.size) { false }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val textView: TextView = view.findViewById(R.id.textView)
        val item = getItem(position)
        textView.text = item
        checkBox.isChecked = checkBoxStates[position]
        val db = FirebaseFirestore.getInstance()

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Remove the item from the list
                items.removeAt(position)

                val timeNow = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

                // Notify the adapter that the data set has changed
                notifyDataSetChanged()

                buttonView.background = context.getDrawable(R.drawable.checkbox_checked)

                db.collection("daily")
                    .whereEqualTo("task_name", item)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("daily").document(document.id)
                                .update(
                                    mapOf(
                                        "checked" to true,
                                        "last_checked" to timeNow
                                    )
                                )
                                .addOnSuccessListener {
                                    Log.d("CustomHomeAdapter", "DocumentSnapshot successfully updated!, time: $timeNow")
                                    homeFragment.updateCounter(homeFragment.view?.findViewById(R.id.tv_counter_home)!!)
                                }
                                .addOnFailureListener {
                                    Log.w("CustomHomeAdapter", "Error updating document", it)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("CustomHomeAdapter", "Error getting documents: ", exception)
                    }

            } else {
                buttonView.background = context.getDrawable(R.drawable.checkbox_background)
            }
        }
        return view
    }
}