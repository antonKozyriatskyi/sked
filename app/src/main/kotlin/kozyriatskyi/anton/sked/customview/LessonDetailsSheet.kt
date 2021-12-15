package kozyriatskyi.anton.sked.customview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.util.find


/**
 * Created by Anton on 15.03.2017.
 */
class LessonDetailsSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "lds"

        private const val USER_TYPE = "type"
        const val USER_TYPE_STUDENT = 1
        const val USER_TYPE_TEACHER = 2

        @IntDef(USER_TYPE_STUDENT, USER_TYPE_TEACHER)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Type

        private const val KEY_LESSON = "lesson"

        fun create(lesson: LessonUi, @Type type: Int): LessonDetailsSheet {
            return LessonDetailsSheet().also {
                it.arguments = bundleOf(
                    KEY_LESSON to lesson,
                    USER_TYPE to type
                )
            }
        }
    }

    private val userType: Int by lazy {
        requireArguments().getInt(USER_TYPE)
    }

    private val lesson: LessonUi by lazy {
        requireArguments().getParcelable(KEY_LESSON)!!
    }

    private lateinit var name: TextView
    private lateinit var type: TextView
    private lateinit var number: TextView
    private lateinit var cabinet: TextView
    private lateinit var who: TextView
    private lateinit var time: TextView
    private lateinit var date: TextView
    private lateinit var addedOn: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = when (userType) {
                USER_TYPE_STUDENT -> R.layout.bottomsheet_lesson_student
                else -> R.layout.bottomsheet_lesson_teacher
        }

        val rootView = inflater.inflate(layoutId, container, false)

        name = rootView.find(R.id.details_name_value)
        type = rootView.find(R.id.details_type_value)
        number = rootView.find(R.id.details_number_value)
        cabinet = rootView.find(R.id.details_cabinet_value)
        who = rootView.find(R.id.details_who_value)
        time = rootView.find(R.id.details_time_value)
        date = rootView.find(R.id.details_date_value)
        addedOn = rootView.find(R.id.details_added_on_value)

        showLesson(lesson)

        return rootView
    }

    @SuppressLint("SetTextI18n")
    private fun showLesson(lesson: LessonUi) {
        name.text = "${lesson.name} (${lesson.shortName})"
        type.text = lesson.type
        type.setTextColor(ContextCompat.getColor(requireContext(), lesson.typeColorRes))
        number.text = lesson.number
        cabinet.text = lesson.cabinet
        who.text = lesson.who
        time.text = lesson.time
        date.text = lesson.shortDate
        addedOn.text = "${lesson.addedOnDate} ${lesson.addedOnTime}"
    }
}