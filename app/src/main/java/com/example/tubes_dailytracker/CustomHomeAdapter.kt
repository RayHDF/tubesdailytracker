package com.example.tubes_dailytracker
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
class CustomHomeAdapter(context: Context, private val resource: Int, private var items: MutableList<String>)
    : ArrayAdapter<String>(context, resource, items) {
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
                // Remove the item from the list
                items.removeAt(position)

                // Notify the adapter that the data set has changed
                notifyDataSetChanged()

                buttonView.background = context.getDrawable(R.drawable.checkbox_checked)
            } else {
                buttonView.background = context.getDrawable(R.drawable.checkbox_background)
            }
        }
        return view
    }
}