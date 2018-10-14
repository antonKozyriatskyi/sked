package kozyriatskyi.anton.sked.audiences.sheet

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class BlockingBottomSheetBehavior<V : View>(context: Context, attrs: AttributeSet?) : BottomSheetBehavior<V>(context, attrs) {

    var isLocked = true

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        var handled = false

        if (!isLocked) handled = super.onInterceptTouchEvent(parent, child, event)

        return handled
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        var handled = false

        if (!isLocked) handled = super.onTouchEvent(parent, child, event)

        return handled
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float): Boolean {
        var handled = false

        if (!isLocked) handled = super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)

        return handled

    }
}