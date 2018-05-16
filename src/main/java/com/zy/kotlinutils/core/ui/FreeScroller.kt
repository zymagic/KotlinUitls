package com.zy.kotlinutils.core.ui

import android.content.Context
import com.zy.kotlinutils.core.select

/**
 * Created by zy on 18-1-31.
 */
class FreeScroller(context: Context, private val scrollView: ScrollView, private val vertical: Boolean = true) : AbsScroller(context, vertical) {

    override fun onUpOrCancel() {
        // ignore
    }

    override fun onFling(velocityX: Int, velocityY: Int) {
        var velocity = vertical.select(velocityY, velocityX)
        var minX = (velocity > 0).select(Int.MIN_VALUE, 0)
        var maxX = (velocity > 0).select(Int.MAX_VALUE, scrollView.getScrollRange())
        scroller.fling(offset, 0, velocity, 0, minX, maxX, 0, 0)
    }

    override fun evaluate(v: Int): Boolean {
        return v >= 0 && v <= scrollView.getScrollRange()
    }

    interface ScrollView {
        fun getScrollRange(): Int
    }
}