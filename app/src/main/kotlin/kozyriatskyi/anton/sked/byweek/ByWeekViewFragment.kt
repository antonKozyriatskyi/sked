package kozyriatskyi.anton.sked.byweek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.schedule.TabsOwner
import kozyriatskyi.anton.sked.util.DateFormatter
import kozyriatskyi.anton.sked.util.find
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

    @Inject
    lateinit var dateFormatter: DateFormatter

    private lateinit var weeksViewPager: ViewPager2

//    private val adapter: WeeksAdapter by lazy {
//        WeeksAdapter(this).also {
//            it.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//        }
//    }
//
    private lateinit var adapter: WeeksAdapter

    private lateinit var tabsOwner: TabsOwner

    private val titleProvider: TabsOwner.TitleProvider by lazy {
        TabsOwner.TitleProvider { position -> adapter.getTitle(position) }
    }

    @ProvidePresenter
    fun providePresenter(): ByWeekViewPresenter {
        Injector.inject(this)
        return presenter
    }

    override fun onHiddenChanged(hidden: Boolean) {
        setupTabsIfNeeded(hidden)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tabsOwner = parentFragment as TabsOwner
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_by_week_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        weeksViewPager = view.find(R.id.byweek_pager_weeks)
        adapter = WeeksAdapter(this).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        weeksViewPager.adapter = adapter

        setupTabsIfNeeded(isHidden)
    }

    override fun showWeekItems(weekItems: List<ByWeekViewItem>) {
        weeksViewPager.offscreenPageLimit = 2
        adapter.update(weekItems)
    }

    override fun showWeekAt(position: Int) {
        weeksViewPager.setCurrentItem(position, false)
    }

    private fun setupTabsIfNeeded(isHidden: Boolean) {
        if (isHidden.not() && ::tabsOwner.isInitialized) {
            tabsOwner.setupWithViewPager(weeksViewPager, titleProvider)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        weeksViewPager.adapter = null
    }
}