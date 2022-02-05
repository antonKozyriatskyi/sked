package kozyriatskyi.anton.sked.customview.stickyheaders

import android.graphics.Canvas
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderItemDecoration(recyclerView: RecyclerView) : RecyclerView.ItemDecoration() {

    private lateinit var headerHolder: RecyclerView.ViewHolder
    private var currentStickyHeaderPosition: Int = RecyclerView.NO_POSITION

    private val headerPositionsByItemPositions = SparseIntArray(7)

    private val adapter: StickyHeaderAdapter<RecyclerView.ViewHolder> = kotlin.run {
        recyclerView.adapter as? StickyHeaderAdapter<RecyclerView.ViewHolder>
                ?: throw IllegalArgumentException("adapter must not be null and must be a descendant of StickyHeaderAdapter")
    }

    init {
        recyclerView.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
                return motionEvent.y <= headerHolder.itemView.height
            }
        })

        val dataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                currentStickyHeaderPosition = RecyclerView.NO_POSITION
                preprocessHeaders(recyclerView)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                onChanged()
            }
        }

        adapter.registerAdapterDataObserver(dataObserver)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topChild = parent.getChildAt(0) ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) return

        val currentHeader = getHeaderViewForItemPosition(topChildPosition, parent) ?: return
        val contactPoint = currentHeader.bottom
        val childInContact = getChildInContact(parent, contactPoint)

        if (childInContact != null && adapter.isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(c, currentHeader, childInContact)
            return
        }

        drawHeader(c, currentHeader)
    }

    private fun getHeaderViewForItemPosition(itemPosition: Int, parent: RecyclerView): View? {
        val headerPosition = headerPositionsByItemPositions[itemPosition]

        if (headerPosition != currentStickyHeaderPosition) {
            currentStickyHeaderPosition = headerPosition
            adapter.bindHeaderViewHolder(headerHolder, headerPosition)
            measureAndLayoutHeader(parent, headerHolder.itemView)
        }

        return headerHolder.itemView
    }

    private fun preprocessHeaders(parent: RecyclerView) {
        headerHolder = adapter.getHeaderViewHolder(parent)

        headerPositionsByItemPositions.clear()

        var itemPosition = adapter.itemCount - 1

        while (itemPosition > 0) {
            val headerPosition = adapter.getHeaderPosition(itemPosition)

            for (i in itemPosition downTo headerPosition - 1) {
                headerPositionsByItemPositions.put(i, headerPosition)
            }

            itemPosition = headerPosition - 1
        }
    }

    private fun drawHeader(c: Canvas, header: View) {
        header.draw(c)
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint && child.top <= contactPoint) {
                // This child overlaps the contactPoint
                return child
            }
        }

        return null
    }

    private fun measureAndLayoutHeader(parent: RecyclerView, view: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val paddingEnd = ViewCompat.getPaddingEnd(parent)
        val paddingStart = ViewCompat.getPaddingStart(parent)

        val horizontalPadding = paddingStart + paddingEnd
        val verticalPadding = parent.paddingTop + parent.paddingBottom

        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, horizontalPadding, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, verticalPadding, view.layoutParams.height)

        view.measure(childWidthSpec, childHeightSpec)

        view.layout(paddingStart, parent.paddingTop, parent.width - paddingEnd, parent.paddingTop + view.measuredHeight)
    }
}