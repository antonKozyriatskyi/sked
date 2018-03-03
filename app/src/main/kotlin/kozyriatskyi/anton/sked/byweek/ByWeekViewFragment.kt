package kozyriatskyi.anton.sked.byweek

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.main.MainActivity
import kozyriatskyi.anton.sked.util.find
import kozyriatskyi.anton.sked.util.inflate
import javax.inject.Inject

/**
 * Created by Anton on 11.08.2017.
 */

class ByWeekViewFragment() : MvpAppCompatFragment(), ByWeekView {

    companion object {
        const val TAG = "ByWeekViewFragment"
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: ByWeekViewPresenter

    private lateinit var weeksViewPager: ViewPager

    @ProvidePresenter
    fun providePresenter(): ByWeekViewPresenter {
        Injector.byWeekViewComponent().inject(this)
        return presenter
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden.not()) {
            // this code smells
            (activity as MainActivity).tabs.setupWithViewPager(weeksViewPager, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container!!.inflate(R.layout.fragment_by_week_view)

        weeksViewPager = rootView.find(R.id.byweek_pager_weeks)

        if (isHidden.not()) {
            // this code smells
            (activity as MainActivity).tabs.setupWithViewPager(weeksViewPager, true)
        }
        return rootView
    }

    override fun showWeeks(dateTitles: Array<String>) {
        val adapter = WeeksAdapter(childFragmentManager, dateTitles)
        weeksViewPager.adapter = adapter
    }

    override fun showNextWeek() {
        weeksViewPager.setCurrentItem(1, false)
    }

    override fun onDestroy() {
        super.onDestroy()
//        val refWatcher = App.getRefWatcher(activity)
//        refWatcher.watch(this)
    }
}