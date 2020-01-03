package kozyriatskyi.anton.sked.byweek

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.main.TabsOwner
import kozyriatskyi.anton.sked.util.find
import kozyriatskyi.anton.sked.util.inflate
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

/**
 * Created by Anton on 11.08.2017.
 */

class ByWeekViewFragment : MvpAppCompatFragment(), ByWeekView {

    companion object {
        const val TAG = "ByWeekViewFragment"
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: ByWeekViewPresenter

    private lateinit var weeksViewPager: ViewPager

    private var tabsOwner: TabsOwner? = null

    @ProvidePresenter
    fun providePresenter(): ByWeekViewPresenter {
        Injector.inject(this)
        return presenter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        tabsOwner = context as TabsOwner
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden.not()) {
            tabsOwner?.setupWithViewPager(weeksViewPager, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container!!.inflate(R.layout.fragment_by_week_view)

        weeksViewPager = rootView.find(R.id.byweek_pager_weeks)

        if (isHidden.not()) {
            tabsOwner?.setupWithViewPager(weeksViewPager, true)
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
}