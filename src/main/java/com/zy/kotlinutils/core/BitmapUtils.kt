package com.zy.kotlinutils.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

/**
 * Created by zy on 18-1-30.
 */

fun Bitmap.averageColor(ignoreGray: Boolean = false) : Int {
    val w = width
    val h = height
    val colors = IntArray(w.times(h))
    var sumA = 0
    var sumR = 0
    var sumG = 0
    var sumB = 0
    var count = 0
    getPixels(colors, 0, width, 0, 0, width, height)
    for (i in 0 .. w) {
        for (j in 0 .. h) {
            val color = colors[i + j * w]
            if (ignoreGray && color.isGray()) {
                continue
            }
            val (a, r, g, b) = color.argb()
            sumA += a
            sumR += r
            sumG += g
            sumB += b
            count++
        }
    }
    if (count == 0) {
        return 0
    }
    return Color.argb(sumA / count, sumR / count, sumG / count, sumB / count)
}

fun View.capture(opaque: Boolean = false): Bitmap? {
    if (width == 0 || height == 0) {
        return null
    }
    val bmp = Bitmap.createBitmap(width, height, if (opaque) Bitmap.Config.RGB_565 else Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    return bmp
}