package com.zy.kotlinutils.core

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Created by zy on 17-12-18.
 */
val UI_HANDLER = Handler(Looper.getMainLooper())

private val EXECUTOR = Executors.newCachedThreadPool { Thread(it, "async") }

private val SCHEDULER = Executors.newScheduledThreadPool(5) { Thread(it, "schedule") }


//--- base methods ---//

fun <T> async(exec: ExecutorService = EXECUTOR, runnable: () -> T): Future<T> {
    return exec.submit(runnable)
}

fun uiThread(f: () -> Unit) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        f()
    } else {
        UI_HANDLER.post({ f() })
    }
}

fun post(handler: Handler = UI_HANDLER, delay: Long = 0, runnable: () -> Unit) = handler.postDelayed(runnable, delay)

fun schedule(delay: Long = 0, task: () -> Unit) = SCHEDULER.schedule(task, delay, TimeUnit.MILLISECONDS)


//--- extend methods for method ---//

fun <T: () -> Unit> T.postRun(handler: Handler = UI_HANDLER, delay: Long = 0) = handler.postDelayed(this, delay)

fun <T: () -> Unit> T.scheduleRun(delay: Long = 0) = SCHEDULER.schedule(this, delay, TimeUnit.MILLISECONDS)

fun <T: () -> Unit> T.UiThreadRun() = uiThread(this)

fun <R, T: () -> R> T.asyncRun(exec: ExecutorService = EXECUTOR) = async(exec, this)


//--- extend methods for object ---//

fun <T> T.uiThreadCall(block: T.() -> Unit) = uiThread { block(this) }

fun <T, R> T.asyncCall(block: T.() -> Future<R>) = async { block(this) }

fun <T> T.postCall(handler: Handler = UI_HANDLER, delay: Long = 0, block: T.() -> Unit) = handler.postDelayed({ block(this) }, delay)

fun <T> T.scheduleCall(delay: Long, block: T.() -> Unit) = SCHEDULER.schedule({ block(this) }, delay, TimeUnit.MILLISECONDS)

