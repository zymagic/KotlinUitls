package com.zy.kotlinutils.core

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import com.zy.kotlinutils.AppInterface
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Created by zy on 18-1-30.
 */
private val density: Float get() = AppInterface.app.resources.displayMetrics.density

fun Int.dp(): Int = times(density).roundToInt()
fun Float.dp(): Int = times(density).roundToInt()
fun Double.dp(): Int = times(density).roundToInt()
fun Long.dp(): Int = times(density).roundToInt()

fun Int.bitmap() : Bitmap {
    return BitmapFactory.decodeResource(AppInterface.app.resources, this)
}
fun Int.color(): Int = AppInterface.app.resources.getColor(this)
fun Int.colorStates(): ColorStateList = AppInterface.app.resources.getColorStateList(this)
fun Int.drawable(): Drawable = AppInterface.app.resources.getDrawable(this)
fun Int.string(vararg obj: Any): String {
    if (obj.isEmpty()) {
        return AppInterface.app.resources.getString(this)
    }
    return AppInterface.app.resources.getString(this, obj)
}
fun Int.dimen(): Int = AppInterface.app.resources.getDimensionPixelSize(this)
fun <V: View> Int.inflateView(context: Context = AppInterface.app): V = LayoutInflater.from(context).inflate(this, null) as V
infix fun <V: View> Context.inflate(layout: Int): V = LayoutInflater.from(this).inflate(layout, null) as V

/**
 * get the current screen width, this may change due to orientation change
 * */
val screenWidth: Int
    get() = AppInterface.app.resources.displayMetrics.widthPixels

/**
 * get the current screen height, this may change due to orientation change
 * */
val screenHeight: Int
    get() = AppInterface.app.resources.displayMetrics.heightPixels

/**
 * get the constant screen width, which means the short axis
 * */
val SCREEN_WIDTH: Int = min(screenWidth, screenHeight)
/**
 * get the constant screen height, which means the long axis
 * */
val SCREEN_HEIGHT: Int = max(screenWidth, screenHeight)

fun testRes() {

}