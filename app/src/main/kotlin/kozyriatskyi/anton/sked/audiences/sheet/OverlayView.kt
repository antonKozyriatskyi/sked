package kozyriatskyi.anton.sked.audiences.sheet

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class OverlayView : View {

    var onTapListener: OnTapListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (alpha != 0f) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                onTapListener?.onTapped()
            }

            return true
        }

        return super.onTouchEvent(event)
    }

    interface OnTapListener {
        fun onTapped()
    }
}