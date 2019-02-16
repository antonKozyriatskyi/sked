package kozyriatskyi.anton.sked.customview.stickyheaders

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup


abstract class StickyHeaderAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    abstract fun getHeaderPosition(itemPosition: Int): Int

    abstract fun getHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun bindHeaderViewHolder(holder: RecyclerView.ViewHolder, headerPosition: Int)

    abstract fun isHeader(itemPosition: Int): Boolean
}