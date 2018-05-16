package com.zy.kotlinutils.core

import com.zy.kotlinutils.core.cache.Cache
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by zy on 17-12-9.
 */
abstract class Loader<K, V, T: Attachment<K, V>> {
    var flashCache: Cache<K, V>? = null
    var backCache: Cache<K, V>? = null
    var taskMap: HashMap<K, MutableList<T>> = HashMap()
    abstract var scheduler: Scheduler
    abstract var mNotifyScheduler: Scheduler

    fun T.checkPendingTask(): MutableList<T> {
        var list = taskMap[this.key]
        if (list == null) {
            list = ArrayList()
            taskMap.put(this.key, list)
        }
        list.add(this)
        return list
    }

    fun load(t: T): T {
        t.obj = flashCache?.get(t.key)
        if (t.obj != null) {
            taskMap.remove(t.key)?.map { it.obj = t.obj }
        } else {
            var list = t.checkPendingTask()
            if (list.size > 1) {
                scheduler.schedule {
                    t.obj = backCache?.get(t.key)
                    taskMap.remove(t.key)?.filter {it != t}?.map {it.obj = t.obj}
                    mNotifyScheduler.schedule {
                        onLoaded(t)
                    }
                }
            }
        }
        return t
    }

    fun syncLoad(t: T): T {
        t.obj = flashCache?.get(t.key) ?: backCache?.get(t.key)
        t.obj?.let { flashCache?.put(t.key, it) }
        return t
    }

    open fun onLoaded(t:T) {}
}

interface Scheduler {
    fun schedule(f: () -> Unit)
}

class Attachment<K, V>(val key: K) {
    private var _obj: V? = null
    var obj: V?
        get() = _obj
        set(value) {
            _obj = value
            if (value != null) {
                onLoaded(this, value)
            }
        }
    open fun onLoaded(attachment: Attachment<K, V>, value: V) {}
}