package com.zy.kotlinutils.core

import android.graphics.Color

/**
 * Created by zy on 18-1-30.
 */

/***
 * alpha of color
 */
val Int.alpha: Int
    get() = Color.alpha(this)
/***
 * red of color
 */
val Int.red: Int
    get() = Color.red(this)
/***
 * green of color
 */
val Int.green: Int
    get() = Color.green(this)
/***
 * blue of color
 */
val Int.blue: Int
    get() = Color.blue(this)
/**
 * gray value of color
 * */
val Int.gray: Int
    get() = (red.times(0.3f) + green.times(0.59f) + blue.times(0.11f)).toInt()

fun Int.argb(): ARGB = ARGB(this)

fun Int.isGray(): Boolean {
    return red == green && red == blue
}

fun Float.mixColor(color1: Int, color2: Int) : Int {
    return Color.argb(
            mix(color1.alpha, color2.alpha),
            mix(color1.red, color2.red),
            mix(color1.green, color2.green),
            mix(color1.blue, color2.blue)
    )
}

data class ARGB(val a: Int, val r: Int, val g: Int, val b: Int) {
    constructor(color: Int) : this(color.alpha, color.red, color.green, color.blue)
}

fun ARGB.color(): Int = Color.argb(a, r, g, b)

fun Float.mixColor(color1: ARGB, color2: ARGB) : Int {
    return Color.argb(
            mix(color1.a, color2.a),
            mix(color1.r, color2.r),
            mix(color1.g, color2.g),
            mix(color1.b, color2.b)
    )
}

fun testUI() {

}