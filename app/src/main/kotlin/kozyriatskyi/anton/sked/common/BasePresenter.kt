package kozyriatskyi.anton.sked.common

import androidx.annotation.CallSuper
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<View: MvpView> : MvpPresenter<View>() {

    protected val scope = MainScope()

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }
}