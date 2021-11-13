package kozyriatskyi.anton.sked.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.customview.LessonDetailsSheet
import kozyriatskyi.anton.sked.customview.stickyheaders.StickyHeaderItemDecoration
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
 * Created by Anton on 11.08.2017.
 */

@Suppress("UNCHECKED_CAST")
class WeekViewFragment : MvpAppCompatFragment(), WeekView,
    WeekLessonsAdapter.OnLessonClickListener {

    companion object {

        private const val ARG_DATES = "WeekViewFragment::dates"

        fun create(dates: List<LocalDate>): WeekViewFragment = WeekViewFragment().apply {
            arguments = bundleOf(ARG_DATES to dates)
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: WeekViewPresenter

    private lateinit var recycler: RecyclerView
    private val adapter by lazy {
        WeekLessonsAdapter(requireContext(), this)
    }

    @ProvidePresenterTag(presenterClass = WeekViewPresenter::class)
    fun provideTag(): String {
        val dates = requireArguments().getSerializable(ARG_DATES) as List<LocalDate>
        return dates.first().toString()
    }

    @ProvidePresenter
    fun providePresenter(): WeekViewPresenter {
        val dates = requireArguments().getSerializable(ARG_DATES) as List<LocalDate>

        Injector.inject(this, dates)
        return presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lessons, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.find<RecyclerView>(R.id.lessons_recycler).also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(StickyHeaderItemDecoration(it))
        }
    }

    override fun onLessonClick(lesson: LessonUi) {
        presenter.onLessonClick(lesson)
    }

    override fun showLessons(lessons: List<DayUi>) {
        adapter.update(lessons)
    }

    override fun showError(message: String) {
        toast(message)
    }

    override fun setPosition(position: Int) {
        recycler.scrollToPosition(position)
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
