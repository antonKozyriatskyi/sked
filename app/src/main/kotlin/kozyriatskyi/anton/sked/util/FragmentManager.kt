package kozyriatskyi.anton.sked.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentOnAttachListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Backbase R&D B.V. on 05.12.2021.
 */

suspend inline fun <reified T : Fragment> FragmentManager.awaitFragmentAttached(): T =
    awaitFragmentAttached { it is T } as T


suspend inline fun FragmentManager.awaitFragmentAttached(
    crossinline predicate: (Fragment) -> Boolean
): Fragment = suspendCoroutine {
    addFragmentOnAttachListener(object : FragmentOnAttachListener {
        override fun onAttachFragment(fragmentManager: FragmentManager, fragment: Fragment) {
            if (!predicate(fragment)) {
                return
            }

            fragmentManager.removeFragmentOnAttachListener(this)

            it.resume(fragment)
        }
    })
}

suspend fun FragmentManager.awaitFragmentCreated(fragment: Fragment): Unit = suspendCoroutine {
    registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            if (f !== fragment) {
                return
            }

            fm.unregisterFragmentLifecycleCallbacks(this)

            it.resume(Unit)
        }
    }, false)
}