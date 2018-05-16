package com.zy.kotlinutils.core

import android.database.Cursor
import android.os.Handler
import android.os.Looper
import java.io.Closeable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by zy on 17-12-18.
 */
val UI_HANDLER = Handler(Looper.getMainLooper())

fun <R> runOnUiThread(f: () -> R) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        f()
    } else {
        UI_HANDLER.post({ f() })
    }
}

fun <R> postInUiThread(delay: Long = 0, f: () -> R) = UI_HANDLER.postDelayed({ f() }, delay)

fun <R> postInHandler(handler: Handler, delay: Long, f:() -> R) = handler.postDelayed({ f() }, delay)

fun <R> runInLooper(f: () -> R) {
    if (Looper.myLooper() != null) {
        f()
    } else {
        runOnUiThread(f)
    }
}

val EXECUTOR = Executors.newCachedThreadPool()

fun Runnable.execute() = EXECUTOR.execute(this)

fun <R> execute(exec: ExecutorService = EXECUTOR, r: () -> R): Future<R> {
    return exec.submit(r)
}

fun <R> async(exec: ExecutorService = EXECUTOR, r: () -> R): AsyncObject<R> {
    return AsyncObject(exec, r)
}

class AsyncObject<out R>(exec: ExecutorService, f: () -> R) {

    private var r: R? = null
    private var executed = false
    private var callback: ((R?) -> Unit)? = null
    private var uiCallback: ((R?) -> Unit)? = null

    init {
        execute(exec) {
            r = f()
            executed = true
            callback?.invoke(r)
            uiCallback?.let {
                runOnUiThread {
                    it.invoke(r)
                }
            }
        }
    }

    fun callback(f: (R?) -> Unit) {
        if (executed) {
            f(r)
        } else {
            callback = f
        }
    }

    fun callbackOnUiThread(f: (R?) -> Unit) {
        if (executed) {
            runOnUiThread { f(r) }
        } else {
            uiCallback = f
        }
    }
}

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

    fun except(f: (ex: Exception) -> Unit): SafeBlock {
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

fun <T> select(vararg a: T, f: ()-> Int): T {
    return a[f()]
}

fun test2() {
    async {
        //do something
        runOnUiThread {

        }
    }.callback {

    }
}