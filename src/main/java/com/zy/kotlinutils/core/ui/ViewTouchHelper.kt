package com.zy.kotlinutils.core.ui

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import kotlin.math.abs

/**
 * Created by zy on 18-1-7.
 */
class ViewTouchHelper(context: Context, private val direction: Int, private var listener: ViewTouchListener) {
    private val touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private val flingThres = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    var lastX: Float = -1f
    var lastY: Float = -1f
    var touchState: Int = TOUCH_STATE_REST
    var isDown: Boolean = false

    var velocityTracker: VelocityTracker? = null

    private var touchValidator: ((Float, Float) -> Boolean)? = null;
    private var isValidTouch: Boolean = false

    companion object {
        const val TOUCH_STATE_REST = 0
        const val TOUCH_STATE_DRAG = 1

        const val DIRECTION_HORIZONTAL_ONLY = 0
        const val DIRECTION_HORIZONTAL_MIXED = 1
        const val DIRECTION_VERTICAL_ONLY = 2
        const val DIRECTION_VERTICAL_MIXED = 4
    }

    fun touchValidator(validator: (Float, Float) -> Boolean) {
        touchValidator = validator
    }

    private fun isValidArea(x: Float, y: Float) : Boolean {
        return touchValidator?.invoke(x, y) ?: true
    }

    fun forwardTouchEvent(event: MotionEvent): Boolean {
        trackMovement(event)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                isValidTouch = isValidArea(lastX, lastY)
                if (isValidTouch) {
                    if (!isDown) {
                        isDown = true
                        touchState = if (listener.onDown()) TOUCH_STATE_DRAG else TOUCH_STATE_REST
                    }
                } else {
                    touchState = TOUCH_STATE_REST
                }
            }
            MotionEvent.ACTION_MOVE -> {
                isDown = false
                var dx = event.x - lastX
                var dy = event.y - lastY
                if (isValidTouch) {
                    if (touchState == TOUCH_STATE_REST) {
                        if (isMoved(dx, dy)) {
                            touchState = TOUCH_STATE_DRAG
                            lastX = event.x
                            lastY = event.y
                            listener.onMoveStart()
                        }
                    } else {
                        lastX = event.x
                        lastY = event.y
                        listener.onMove(dx.toInt(), dy.toInt())
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDown = false
                if (isValidTouch) {
                    if (touchState == TOUCH_STATE_DRAG
                            && event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_UP) {
                        val tracker = velocityTracker
                        tracker?.computeCurrentVelocity(1000)
                        var vx: Float = tracker?.xVelocity ?: 0f
                        var vy: Float = tracker?.yVelocity ?: 0f
                        if (isFling(vx, vy)) {
                            listener.onFling(vx.toInt(), vy.toInt())
                        } else {
                            listener.onUpOrCancel()
                        }
                    } else {
                        listener.onUpOrCancel()
                    }
                }
                releaseTracker()
                touchState = TOUCH_STATE_REST
                isValidTouch = false
            }
            else -> {
                isDown = false
            }
        }
        return touchState != TOUCH_STATE_REST
    }

    private fun isMoved(dx: Float, dy: Float) : Boolean {
        return when (direction) {
            DIRECTION_HORIZONTAL_ONLY -> {
                abs(dx) > touchSlop
            }
            DIRECTION_HORIZONTAL_MIXED -> {
                abs(dx) > abs(dy) && abs(dx) > touchSlop
            }
            DIRECTION_VERTICAL_ONLY -> {
                abs(dy) > touchSlop
            }
            DIRECTION_VERTICAL_MIXED -> {
                abs(dy) > abs(dx) && abs(dy) > touchSlop
            }
            else -> false
        }
    }

    private fun isFling(vx: Float, vy: Float) : Boolean {
        return when (direction) {
            DIRECTION_HORIZONTAL_ONLY, DIRECTION_HORIZONTAL_MIXED -> {
                abs(vx) > flingThres
            }
            DIRECTION_VERTICAL_ONLY, DIRECTION_VERTICAL_MIXED -> {
                abs(vy) > flingThres
            }
            else -> false
        }
    }

    private fun trackMovement(event: MotionEvent) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker?.addMovement(event)
    }

    private fun releaseTracker() {
        velocityTracker?.recycle()
        velocityTracker = null
    }

    interface ViewTouchListener {
        fun onDown() : Boolean
        fun onMoveStart()
        fun onMove(dx: Int, dy: Int)
        fun onFling(velocityX: Int, velocityY: Int) {}
        fun onUpOrCancel()
    }

    interface TouchValidator {
        fun isValidTouch(x: Float, y: Float): Boolean
    }
}