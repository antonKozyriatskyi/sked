package kozyriatskyi.anton.sked.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.snackbar.Snackbar

class MoveUpwardBehavior(context: Context, attrs: AttributeSet) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: View, dependency: View): Boolean {
        val translationY = Math.min(0f, dependency.translationY - dependency.height)
        child.translationY = translationY
        return true
    }
}