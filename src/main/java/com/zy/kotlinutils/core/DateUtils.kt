package com.zy.kotlinutils.core

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zy on 18-2-9.
 */

val DATEFORMAT = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

infix fun Long.dateDiff(other: Long): Int {
    var c1: Calendar = Calendar.getInstance()
    c1.time = Date(this)

    var c2: Calendar = Calendar.getInstance()
    c2.time = Date(other)

    return (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 365 +
            c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR)
}

fun String.toDateLong() : Long {
    return try {
        DATEFORMAT.parse(this).time
    } catch (e: Exception) {
        -1
    }
}