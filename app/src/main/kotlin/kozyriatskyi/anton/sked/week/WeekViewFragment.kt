package kozyriatskyi.anton.sked.week

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.customview.LessonDetailsSheet
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.util.find
import kozyriatskyi.anton.sked.util.inflate
import kozyriatskyi.anton.sked.util.toast
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by Anton on 11.08.2017.
 */

class WeekViewFragment : MvpAppCompatFragment(), WeekView, WeekLessonsAdapter.OnLessonClickListener {

    companion object {
        private const val EXTRA_WEEK_NUM = "week_num"

        fun create(weekNumber: Int): WeekViewFragment {
            val fragment = WeekViewFragment()
            val arguments = Bundle()
            arguments.putInt(EXTRA_WEEK_NUM, weekNumber)
            fragment.arguments = arguments
            return fragment
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: WeekViewPresenter

    private var weekNumber: Int by Delegates.notNull()

    private lateinit var recycler: RecyclerView

    @ProvidePresenterTag(presenterClass = WeekViewPresenter::class)
    fun provideTag(): String = arguments!!.getInt(EXTRA_WEEK_NUM).toString()

    @ProvidePresenter
    fun providePresenter(): WeekViewPresenter {
        val weekNumber = arguments?.getInt(EXTRA_WEEK_NUM) ?:  throw  IllegalArgumentException("Arguments must not be null " +
                "and must contain EXTRA_WEEK_NUM")
        Injector.weekViewComponent(weekNumber).inject(this)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val arguments = arguments ?: throw  IllegalArgumentException("Arguments must not be null " +
                "and must contain EXTRA_WEEK_NUM")

        weekNumber = arguments.getInt(EXTRA_WEEK_NUM)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container!!.inflate(R.layout.fragment_week_lessons)

        recycler = rootView.find(R.id.week_recycler_lessons)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = WeekLessonsAdapter(this)

        return rootView
    }

    override fun onLessonClick(lesson: LessonUi) {
        presenter.onLessonClick(lesson)
    }

    override fun showLessons(lessons: List<DayUi>) {
        (recycler.adapter as WeekLessonsAdapter).update(lessons)
    }

    override fun showError(message: String) {
        toast(message)
    }

    override fun setPosition(position: Int) {
        recycler.scrollToPosition(position)
    }

    override fun showStudentLessonDetails(lesson: LessonUi) {
        val sheet = LessonDetailsSheet.create(lesson, LessonDetailsSheet.USER_TYPE_STUDENT)
        sheet.show(activity!!.supportFragmentManager, LessonDetailsSheet.TAG)
    }

    override fun showTeacherLessonDetails(lesson: LessonUi) {
        val sheet = LessonDetailsSheet.create(lesson, LessonDetailsSheet.USER_TYPE_TEACHER)
        sheet.show(activity!!.supportFragmentManager, LessonDetailsSheet.TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
//        val refWatcher = App.getRefWatcher(activity)
//        refWatcher.watch(this)
    }
}

