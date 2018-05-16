package com.zy.kotlinutils.core.ui

import android.content.Context
import com.zy.kotlinutils.core.clamp
import com.zy.kotlinutils.core.maxTo
import com.zy.kotlinutils.core.minTo
import com.zy.kotlinutils.core.select
import kotlin.math.absoluteValue
import kotlin.math.pow

/**
 * Created by zy on 18-1-7.
 */
class PageScroller(context: Context, val pageView: PageView, private val vertical: Boolean = false) : AbsScroller(context, vertical) {

    var currentScreen: Int = 0
    private var nextScreen: Int = -1
    private var pageListener: PageListener? = null

    override fun onUpOrCancel() {
        var targetScreen = ((offset + pageView.getPageWidth() / 2) / pageView.getPageWidth())
                .clamp(0, pageView.getPageCount() - 1)
        snapTo(targetScreen, 0)
    }

    override fun onFling(velocityX: Int, velocityY: Int) {
        when (vertical.select(velocityY, velocityX) > 0) {
            true ->
                snapTo(currentScreen - 1 minTo 0, velocityY)
            else ->
                snapTo(currentScreen + 1 maxTo pageView.getPageCount() - 1, velocityY)
        }
    }

    private fun snapTo(page : Int, velockty: Int) {
        nextScreen = page
        var target = page.times(pageView.getPageWidth())
        var distance = target - offset
        scroller.startScroll(offset, 0, distance, 0, computeDuration(distance, velockty))
        pageListener?.onPageSwitching(currentScreen, nextScreen)
    }

    private fun computeDuration(distance: Int, velocity: Int): Int {
        if (velocity == 0) {
            return (1 - (distance.absoluteValue.toFloat() / pageView.getPageWidth()).pow(2)).times(500).toInt()
        }
        return (distance.absoluteValue / 2f / velocity).toInt().clamp(300, 500)
    }

    override fun onScrollFinished() {
        if (nextScreen != -1) {
            currentScreen = nextScreen
            nextScreen = -1
            pageListener?.onPageSwitched(currentScreen)
        }
    }

    fun setPageListener(listener: PageListener) {
        this.pageListener = listener
    }

    interface PageView {
        fun getPageWidth() : Int
        fun getPageCount() : Int
    }

    interface PageListener {
        fun onPageSwitching(from: Int, to: Int)
        fun onPageSwitched(to: Int)
    }
}