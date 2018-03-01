package kozyriatskyi.anton.sked.ui.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.adapter.DaysAdapter
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.presentation.presenter.ByDayViewPresenter
import kozyriatskyi.anton.sked.presentation.view.ByDayView
import kozyriatskyi.anton.sked.ui.activity.MainActivity
import kozyriatskyi.anton.sked.util.find
import kozyriatskyi.anton.sked.util.inflate
import javax.inject.Inject



/**
 * Created by Anton on 04.08.2017.
 */
class ByDayViewFragment : MvpAppCompatFragment(), ByDayView {

    companion object {
        const val TAG = "ByDayViewFragment"
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: ByDayViewPresenter

    private lateinit var adapter: DaysAdapter
    private lateinit var daysViewPager: ViewPager

    @ProvidePresenter
    fun providePresenter(): ByDayViewPresenter  {
        Injector.byDayViewComponent().inject(this)
        return presenter
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden.not()) {
            // this code smells
            (activity as MainActivity).tabs.setupWithViewPager(daysViewPager, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container!!.inflate(R.layout.fragment_by_day_view)
        daysViewPager = rootView.find(R.id.byday_pager_days)

        if (isHidden.not()) {
            // this code smells
            (activity as MainActivity).tabs.setupWithViewPager(daysViewPager, true)
        }
        return rootView
    }

    override fun showDays(isNextWeek: Boolean) {
        val tabTitles = context!!.resources.getStringArray(R.array.days_of_week)
        adapter = DaysAdapter(childFragmentManager, tabTitles, isNextWeek)
        daysViewPager.adapter = adapter
    }

    override fun addTab() {
        adapter.addTab()
    }

    override fun removeTab() {
        adapter.removeTab()
    }

    override fun setTodayPosition(todayPosition: Int) {
        daysViewPager.setCurrentItem(todayPosition, false)
    }

    override fun onDestroy() {
        super.onDestroy()
//        val refWatcher = App.getRefWatcher(activity)
//        refWatcher.watch(this)
    }
}