package kozyriatskyi.anton.sked.main

import android.os.Bundle
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentOnAttachListener
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.whenCreated
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.launch
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.moxy.MvpAppCompatActivity
import kozyriatskyi.anton.sked.util.awaitFragmentAttached
import kozyriatskyi.anton.sked.util.awaitFragmentCreated
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MainActivity : MvpAppCompatActivity(), MainView {

    @Inject
    @InjectPresenter
    internal lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        Injector.inject(this)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(savedInstanceState)

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
    }

    private fun setup(savedInstanceState: Bundle?) {
        lifecycle.coroutineScope.launch {
            val fragment = supportFragmentManager.awaitFragmentAttached<NavHostFragment>()
            supportFragmentManager.awaitFragmentCreated(fragment)

            Injector.update(fragment.navController)
            dispatchMvpDelegateOnCreate(savedInstanceState)

            presenter.updateLocale(ConfigurationCompat.getLocales(resources.configuration)[0])
        }
    }
}
