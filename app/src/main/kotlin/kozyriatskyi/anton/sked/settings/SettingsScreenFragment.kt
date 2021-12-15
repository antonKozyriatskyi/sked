package kozyriatskyi.anton.sked.settings

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger
import kozyriatskyi.anton.sked.util.setGone
import javax.inject.Inject

/**
 * Created by Backbase R&D B.V. on 27.11.2021.
 */
class SettingsScreenFragment : Fragment(R.layout.fragment_settings_screen) {

    @Inject
    lateinit var scheduleUpdateTimeLogger: ScheduleUpdateTimeLogger

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialToolbar>(R.id.settings_toolbar).setNavigationOnClickListener {
            navigator.pop()
        }

        if (childFragmentManager.findFragmentByTag(SettingsFragment.TAG) == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.settings_layout_container, SettingsFragment(), SettingsFragment.TAG)
                .commit()
        }

        val timeTitle = view.findViewById<TextView>(R.id.settings_tv_update_time_title)
        val timeValue = view.findViewById<TextView>(R.id.settings_tv_update_time)

        val time = scheduleUpdateTimeLogger.getTime()

        if (time == null) {
            timeTitle.setGone()
            timeValue.setGone()
        } else {
            timeValue.text = time
        }
    }
}