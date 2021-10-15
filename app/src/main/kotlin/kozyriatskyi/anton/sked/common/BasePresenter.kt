package kozyriatskyi.anton.sked.common

import androidx.annotation.CallSuper
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import moxy.MvpPresenter
import moxy.MvpView

/**
 * Created by Backbase R&D B.V. on 11.10.2021.
 */
abstract class BasePresenter<View: MvpView> : MvpPresenter<View>() {

    protected val scope = MainScope()

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }
}