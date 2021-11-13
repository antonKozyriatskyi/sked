package kozyriatskyi.anton.sked.byday

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
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
 * Created by Anton on 04.08.2017.
 */
class ByDayViewFragment : MvpAppCompatFragment(), ByDayView {

    companion object {
        const val TAG = "ByDayViewFragment"
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: ByDayViewPresenter

    private val adapter: DaysAdapter by lazy {
        DaysAdapter(this)
    }
    private lateinit var daysViewPager: ViewPager2

    private lateinit var tabsOwner: TabsOwner

    private val titleProvider: TabsOwner.TitleProvider by lazy {
        val context = requireContext()
        TabsOwner.TitleProvider { position -> adapter.getTitle(context, position) }
    }

    @ProvidePresenter
    fun providePresenter(): ByDayViewPresenter {
        Injector.inject(this)

        return presenter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        tabsOwner = context as TabsOwner
    }

    override fun onHiddenChanged(hidden: Boolean) {
        setupTabsIfNeeded(hidden)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_by_day_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        daysViewPager = view.find(R.id.byday_pager_days)
        daysViewPager.adapter = adapter

        setupTabsIfNeeded(isHidden)
    }

    override fun showDays(days: List<ByDayViewItem>) {
        daysViewPager.offscreenPageLimit = days.size / 2
        adapter.update(days)
    }

    override fun showDayAt(todayPosition: Int) {
        daysViewPager.setCurrentItem(todayPosition, false)
    }

    private fun setupTabsIfNeeded(isHidden: Boolean) {
        if (isHidden.not() && ::tabsOwner.isInitialized) {
            tabsOwner.setupWithViewPager(daysViewPager, titleProvider)
        }
    }
}