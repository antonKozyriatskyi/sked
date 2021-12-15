package kozyriatskyi.anton.sked.intro

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.login.LoginView
import kozyriatskyi.anton.sked.navigation.Destination
import kozyriatskyi.anton.sked.navigation.Navigator
import javax.inject.Inject

class IntroFragment : Fragment(R.layout.fragment_intro) {

    companion object {
        private const val ARG_ALLOW_BACK_NAVIGATION = "arg_can_go_back"

        fun createArgs(allowBackNavigation: Boolean): Bundle {
            return bundleOf(ARG_ALLOW_BACK_NAVIGATION to allowBackNavigation)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private val allowBackNavigation: Boolean by lazy {
        arguments?.getBoolean(ARG_ALLOW_BACK_NAVIGATION) ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.inject(this)

        view.findViewById<Button>(R.id.intro_button_student)
            .setOnClickListener {
                navigator.goTo(Destination.Login(LoginView.UserType.STUDENT))
            }

        view.findViewById<Button>(R.id.intro_button_teacher)
            .setOnClickListener {
                navigator.goTo(Destination.Login(LoginView.UserType.TEACHER))
            }

        if (allowBackNavigation) {
            view.findViewById<MaterialToolbar>(R.id.intro_toolbar).also { toolbar ->
                toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                toolbar.setNavigationOnClickListener { navigator.pop() }
            }
        }
    }
}