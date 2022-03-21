package kozyriatskyi.anton.sked.customview

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IntDef
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.storage.StorageManager
import kozyriatskyi.anton.sked.util.find
import kotlin.properties.Delegates


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
            val manager = StorageManager.get()
            val storage = manager.obtainStorage(TAG)
            storage.save(KEY_LESSON, lesson)
            storage.save(USER_TYPE, type)
            return LessonDetailsSheet()
        }
    }

    private var userType: Int by Delegates.notNull()

    private lateinit var name: TextView
    private lateinit var type: TextView
    private lateinit var number: TextView
    private lateinit var cabinet: TextView
    private lateinit var who: TextView
    private lateinit var time: TextView
    private lateinit var date: TextView
    private lateinit var addedOn: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val storageManager = StorageManager.get()
        val storage = storageManager.obtainStorage(TAG)
        userType = storage.getInt(USER_TYPE)
        val layoutId = if (userType == USER_TYPE_STUDENT) R.layout.bottomsheet_lesson_student else R.layout.bottomsheet_lesson_teacher
        val rootView = inflater.inflate(layoutId, container, false)

        name = rootView.find<TextView>(R.id.details_name_value)
        type = rootView.find<TextView>(R.id.details_type_value)
        number = rootView.find<TextView>(R.id.details_number_value)
        cabinet = rootView.find<TextView>(R.id.details_cabinet_value)
        who = rootView.find<TextView>(R.id.details_who_value)
        time = rootView.find<TextView>(R.id.details_time_value)
        date = rootView.find<TextView>(R.id.details_date_value)
        addedOn = rootView.find<TextView>(R.id.details_added_on_value)

        val lesson = storage.get<LessonUi>(KEY_LESSON)

        if (lesson != null) {
            showLesson(lesson)
        }

        return rootView
    }

    private fun showLesson(lesson: LessonUi) {
        name.text = "${lesson.name} (${lesson.shortName})"
        type.text = lesson.type
        type.setTextColor(MaterialColors.getColor(type, lesson.typeColorAttr))
        number.text = lesson.number
        cabinet.text = lesson.cabinet
        who.text = lesson.who
        time.text = lesson.time
        date.text = lesson.shortDate
        addedOn.text = "${lesson.addedOnDate} ${lesson.addedOnTime}"
    }

    override fun onCancel(dialog: DialogInterface) {
        StorageManager.get().releaseStorage(TAG) // User closed the sheet, so can release the storage
        super.onCancel(dialog)
    }
}