package com.zy.kotlinutils.core.ui

import android.content.Context
import android.view.MotionEvent
import android.widget.Scroller
import com.zy.kotlinutils.core.select

/**
 * Created by zy on 18-1-8.
 */
abstract class AbsScroller(context: Context, private val vertical: Boolean = false) : ViewTouchHelper.ViewTouchListener {

    private val mTouchHelper: ViewTouchHelper = ViewTouchHelper(context,
            vertical.select(ViewTouchHelper.DIRECTION_VERTICAL_MIXED, ViewTouchHelper.DIRECTION_HORIZONTAL_MIXED),
            this)

    protected var scroller: Scroller = Scroller(context)
    protected var offset: Int = 0

    fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return mTouchHelper.forwardTouchEvent(event)
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        mTouchHelper.forwardTouchEvent(event)
        return true
    }

    override fun onDown(): Boolean {
        if (!scroller.isFinished) {
            scroller.abortAnimation()
            return true
        }
        return false
    }

    override fun onMoveStart() {
        //TODO disallow parent intercept
    }

    override fun onMove(dx: Int, dy: Int) {
        offset += vertical.select(dy, dx)
    }

    fun computeScroll(): Int {
        if (scroller.computeScrollOffset()) {
            if (evaluate(scroller.currX)) {
                offset = scroller.currX
            } else {
                onScrollFinished()
            }
        } else {
            onScrollFinished()
        }
        return offset
    }


    open fun evaluate(v: Int): Boolean {
        return true
    }

    open fun onScrollFinished() {

    }
}