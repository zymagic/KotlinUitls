package com.zy.kotlinutils.core

import android.database.Cursor
import android.os.Handler
import android.os.Looper
import java.io.Closeable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

/**
 * Created by zy on 17-12-18.
 */

fun <T: Closeable> T.closeQuietly() {
    safe { close() }
}

fun Cursor.closeQuietly() {
    safe { close() }
}

fun safe(t: () -> Unit): SafeBlock {
    return SafeBlock(t)
}

class SafeBlock(t: () -> Unit) {

    private var e: Exception? = null

    init {
        try {
            t()
        } catch (e: Exception) {
            this.e = e
        }
    }

    fun except(f: (Exception) -> Unit): SafeBlock {
        e?.let(f)
        return this
    }

    fun forward(f: () -> Unit) {
        f()
    }
}

fun <T> Boolean.select(a: T, b: T) : T {
    return if (this) a else b
}

fun <T> select(a: T, b: T, f: () -> Boolean): T {
    return f().select(a, b)
}

fun test2() {
    async {
        //do something
        uiThread {

        }
    }
}