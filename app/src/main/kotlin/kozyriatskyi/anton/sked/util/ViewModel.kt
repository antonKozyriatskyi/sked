package kozyriatskyi.anton.sked.util

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Created by Backbase R&D B.V. on 13.03.2022.
 */

inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(
    crossinline factory: () -> VM
): Lazy<VM> {
    return viewModels { createFactory(factory) }
}

inline fun <reified VM : ViewModel> Fragment.injectViewModel(
    crossinline factory: () -> VM
): Lazy<VM> {
    return viewModels { createFactory(factory) }
}

@Suppress("MissingJvmstatic")
@Composable
inline fun <reified VM : ViewModel> injectViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    crossinline factory: () -> VM
): VM = viewModel(
    VM::class.java,
    viewModelStoreOwner,
    key,
    createFactory(factory)
)

@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> createFactory(crossinline factory: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = factory() as T
    }
}