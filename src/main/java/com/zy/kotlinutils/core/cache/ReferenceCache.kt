package com.zy.kotlinutils.core.cache

import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by zy on 17-12-8.
 */
abstract class ReferenceCache<K, V, R: Reference<V>>: AbsCache<K, V> {

    var mReferenceMap: HashMap<K, R>
    var mReferenceQueue: ReferenceQueue<V> = ReferenceQueue()

    fun HashMap<K, R>.dump(): Map<K, V> {
        var ret = HashMap<K, V>(this.size)
        for (key in this.keys) {
            var v = this[key]?.get() ?: continue
            ret.put(key, v)
        }
        return ret
    }

    constructor() {
        mReferenceMap = HashMap()
    }

    constructor(initialSize: Int) {
        mReferenceMap = HashMap(initialSize)
    }

    override fun all(): Map<K, V> {
        trim()
        return mReferenceMap.dump()
    }

    override fun onPut(key: K, value: V) {
        trim()
        var r = ref(key, value)
        mReferenceMap[key] = r
    }

    override fun onGet(key: K): V? {
        trim()
        return mReferenceMap[key]?.get()
    }

    override fun onRemove(key: K): V? {
        trim()
        return mReferenceMap.remove(key)?.get()
    }

    private fun trim() {
        while (true) {
            val r = mReferenceQueue.poll()
            r?.clear() ?: break
            mReferenceMap.remove(keyOf(r as R))
            r.get()?.let { onClear(it) }
        }
    }

    abstract fun ref(key: K, value: V, queue: ReferenceQueue<V> = mReferenceQueue): R
    abstract fun keyOf(ref: R): K

    open fun onClear(value: V) {}
}

open class SoftCache<K, V> : ReferenceCache<K, V, SoftReference<V>> {
    constructor() : super()
    constructor(initialSize: Int) : super(initialSize)

    override fun ref(key: K, value: V, queue: ReferenceQueue<V>): SoftReference<V> {
        return MySoftReference(key, value, queue)
    }

    override fun keyOf(ref: SoftReference<V>): K {
        return (ref as SoftCache<K, V>.MySoftReference).key
    }

    inner class MySoftReference(val key: K, value: V, queue: ReferenceQueue<V>) : SoftReference<V>(value, queue)
}

class WeakCache<K, V> : ReferenceCache<K, V, WeakReference<V>> {
    constructor() : super()
    constructor(initialSize: Int) : super(initialSize)

    override fun ref(key: K, value: V, queue: ReferenceQueue<V>): WeakReference<V> {
        return MyWeakReference(key, value, queue)
    }

    override fun keyOf(ref: WeakReference<V>): K {
        return (ref as WeakCache<K, V>.MyWeakReference).key
    }

    inner class MyWeakReference(val key: K, value: V, queue: ReferenceQueue<V>) : WeakReference<V>(value, queue)
}