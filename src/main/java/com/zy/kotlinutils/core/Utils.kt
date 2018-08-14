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

fun <T : Closeable> T.closeQuietly() {
    safe { close() }
}

fun Cursor.closeQuietly() {
    safe { close() }
}

fun safe(t: () -> Unit): SafeBlock = SafeBlock(t)

class SafeBlock(t: () -> Unit) {

    internal var e: Exception? = try {
        t()
        null
    } catch (e: Exception) {
        e
    }
}

fun SafeBlock.log(): SafeBlock {
    e?.printStackTrace()
    return this
}

fun SafeBlock.or(f: (Exception) -> Unit): SafeBlock {
    e?.let(f)
    return this
}

fun SafeBlock.forward(f: () -> Unit) = f()

fun <T> Boolean.select(a: T, b: T): T = if (this) a else b

fun <T, R: () -> Boolean> R.select(a: T, b: T): T = this.invoke().select(a, b)

fun <T> select(a: T, b: T, f: () -> Boolean) : T = f().select(a, b)

fun Cursor.manage(f: Cursor.() -> Unit) {
    f()
    closeQuietly()
}

fun test2() {
    async {
        //do something
        uiThread {

        }
    }
}