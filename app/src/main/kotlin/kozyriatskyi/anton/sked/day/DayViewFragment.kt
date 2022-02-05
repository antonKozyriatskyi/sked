package kozyriatskyi.anton.sked.day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.customview.LessonDetailsSheet
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.util.find
import kozyriatskyi.anton.sked.util.inflate
import kozyriatskyi.anton.sked.util.toast
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import moxy.presenter.ProvidePresenterTag
import java.time.LocalDate
import javax.inject.Inject


/**
 * Created by Anton on 01.08.2017.
 */
class DayViewFragment : MvpAppCompatFragment(), DayView, DayLessonsAdapter.OnLessonClickListener {

    companion object {
        private const val ARG_DATE = "DayViewFragment::date"

        fun create(date: LocalDate): DayViewFragment = DayViewFragment().apply {
            arguments = bundleOf(ARG_DATE to date)
        }
    }

    private val adapter: DayLessonsAdapter by lazy {
        DayLessonsAdapter(this)
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: DayViewPresenter

    @ProvidePresenterTag(presenterClass = DayViewPresenter::class)
    fun provideTag(): String {
        val date = requireArguments().getSerializable(ARG_DATE) as LocalDate
        return date.toString()
    }

    @ProvidePresenter
    fun providePresenter(): DayViewPresenter {
        val date = requireArguments().getSerializable(ARG_DATE) as LocalDate
        Injector.inject(this, date)

        return presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_lessons, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.find<RecyclerView>(R.id.lessons_recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    override fun onLessonClick(lesson: LessonUi) {
        presenter.onLessonClick(lesson)
    }

    override fun showDay(day: DayUi) {
        adapter.updateData(day)
    }

    override fun showError(message: String) {
        toast(message)
    }

    override fun showStudentLessonDetails(lesson: LessonUi) {
        val sheet = LessonDetailsSheet.create(lesson, LessonDetailsSheet.USER_TYPE_STUDENT)
        sheet.show(requireActivity().supportFragmentManager, LessonDetailsSheet.TAG)
    }

    override fun showTeacherLessonDetails(lesson: LessonUi) {
        val sheet = LessonDetailsSheet.create(lesson, LessonDetailsSheet.USER_TYPE_TEACHER)
        sheet.show(requireActivity().supportFragmentManager, LessonDetailsSheet.TAG)
    }
}