import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.tubes_dailytracker.R

class MyListAdapter(context: Context, private val resource: Int, private val items: List<String>) :
    ArrayAdapter<String>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val textView = view.findViewById<TextView>(R.id.textView)

        textView.text = items[position]

        // Set an OnCheckedChangeListener
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Checkbox is checked
                Log.d("MyListAdapter", "Checkbox is checked kms")
                buttonView.background = context.getDrawable(R.drawable.checkbox_checked)

            } else {
                // Checkbox is unchecked
                Log.d("MyListAdapter", "Checkbox is unchecked")
                buttonView.background = context.getDrawable(R.drawable.checkbox_background)
            }
        }

        return view
    }
    }