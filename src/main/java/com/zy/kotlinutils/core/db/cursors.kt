package com.zy.kotlinutils.core.db

import android.database.Cursor
import com.zy.kotlinutils.core.closeQuietly

fun <T> Cursor.map(f: (Cursor) -> T): List<T> {
    val ret = ArrayList<T>(count)
    while (moveToNext()) {
        ret.add(f(this))
    }
    return ret
}

fun Cursor.manage(f: Cursor.() -> Unit) {
    f()
    closeQuietly()
}