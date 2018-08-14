package com.zy.kotlinutils.core.rx

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

val IO_EXECUTOR = Executors.newFixedThreadPool(5) { Thread(it, "rx-io") }
val HTTP_EXECUTOR = Executors.newCachedThreadPool { Thread(it, "rx-http") }
val WORK_EXECUTOR = Executors.newScheduledThreadPool(5) { Thread(it, "rx-work") }
val UI_HANDLER = Handler(Looper.getMainLooper())

abstract class AbstractScheduler : Scheduler {

    private val taskMap = HashMap<Int, Task>()

    override fun execute(delay: Long, f: () -> Unit): Int {
        val task = Task(f, taskMap)
        val hash = task.hashCode()
        taskMap[hash] = task
        doExecute(delay, task)
        return hash
    }

    protected abstract fun doExecute(delay: Long, f: Task)

    override fun cancel(id: Int) {
        taskMap.remove(id)
    }

}

class Task(private val f: () -> Unit, private val map: HashMap<Int, Task>) : Runnable {

    override fun run() {
        map.remove(hashCode())
        f.invoke()
    }
}

val IO = object : AbstractScheduler() {
    override fun doExecute(delay: Long, f: Task) {
        IO_EXECUTOR.submit(f)
    }
}

val HTTP = object : AbstractScheduler() {
    override fun doExecute(delay: Long, f: Task) {
        HTTP_EXECUTOR.submit(f)
    }
}

val MAIN = object : AbstractScheduler() {
    override fun doExecute(delay: Long, f: Task) {
        UI_HANDLER.post(f)
    }
}

val CURRENT = object : AbstractScheduler() {
    override fun doExecute(delay: Long, f: Task) {
        f.run()
    }
}

val WORK = object : AbstractScheduler() {
    override fun doExecute(delay: Long, f: Task) {
        WORK_EXECUTOR.schedule(f, delay, TimeUnit.MILLISECONDS)
    }
}