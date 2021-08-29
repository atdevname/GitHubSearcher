package com.atdev.githubproject.components.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.coroutineScope

class ListEmptyHasRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private lateinit var emptyView: View

    fun checkIfEmpty() {
        val emptyViewVisible = this.adapter?.itemCount == 0
        emptyView.visibility = if (emptyViewVisible) VISIBLE else GONE
        this.visibility = if (emptyViewVisible) GONE else VISIBLE
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        this.adapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        this.adapter?.registerAdapterDataObserver(observer)
    }

    private val observer = object : AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {}
        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {}
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {}
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {}
    }

}