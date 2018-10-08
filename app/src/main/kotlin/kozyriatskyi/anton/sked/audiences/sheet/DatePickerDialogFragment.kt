package kozyriatskyi.anton.sked.audiences.sheet

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import java.util.*

class DatePickerDialogFragment : DialogFragment() {

    companion object {
        private const val TAG = "DatePickerDialog"

        fun show(supportFragmentManager: FragmentManager, initialDate: Calendar) {
            val fragment = DatePickerDialogFragment()
            fragment.date = initialDate
            fragment.show(supportFragmentManager, TAG)
        }
    }

    private lateinit var onDateSetListener: DatePickerDialog.OnDateSetListener
    private var date: Calendar = Calendar.getInstance()

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        onDateSetListener = context as DatePickerDialog.OnDateSetListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)
        val day = date.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(context, onDateSetListener, year, month, day)
    }
}