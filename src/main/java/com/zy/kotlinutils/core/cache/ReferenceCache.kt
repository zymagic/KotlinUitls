package com.zy.kotlinutils.core.cache

import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import kotlin.collections.HashMap

/**
 * Created by zy on 17-12-8.
 */
abstract class ReferenceCache<K, V, R: Reference<V>> : AbsCache<K, V> {

    private val referenceMap: HashMap<K, R>
    private val referenceQueue: ReferenceQueue<V> = ReferenceQueue()

    private fun HashMap<K, R>.dump() : Map<K, V> {
        val ret = HashMap<K, V>()
        forEach {
            it.value.get()?.let { v ->
                ret[it.key] = v
            }
        }
        return ret
    }

    constructor() {
        referenceMap = HashMap()
    }

    constructor(initialSize: Int) {
        referenceMap = HashMap(initialSize)
    }

    override fun all(): Map<K, V> {
        trim()
        return referenceMap.dump()
    }

    override fun onPut(key: K, value: V) {
        trim()
        val r = ref(key, value)
        referenceMap[key] = r
    }

    override fun onGet(key: K): V? {
        trim()
        return referenceMap[key]?.get()
    }

    override fun onRemove(key: K): V? {
        trim()
        return referenceMap.remove(key)?.get()
    }

    private fun trim() {
        while (true) {
            val r = referenceQueue.poll()
            r?.clear() ?: break
            referenceMap.remove(keyOf(r as R))
            r.get()?.let { onClear(it) }
        }
    }

    abstract fun ref(key: K, value: V, queue: ReferenceQueue<V> = referenceQueue): R
    abstract fun keyOf(ref: R): K

    open fun onClear(value: V) {}
}

open class SoftCache<K, V> : ReferenceCache<K, V, KeySoftReference<K, V>> {
    constructor() : super()
    constructor(initialSize: Int) : super(initialSize)

    override fun ref(key: K, value: V, queue: ReferenceQueue<V>): KeySoftReference<K, V> = KeySoftReference(key, value, queue)

    override fun keyOf(ref: KeySoftReference<K, V>): K = ref.key
}

class WeakCache<K, V> : ReferenceCache<K, V, KeyWeakReference<K, V>> {
    constructor() : super()
    constructor(initialSize: Int) : super(initialSize)

    override fun ref(key: K, value: V, queue: ReferenceQueue<V>): KeyWeakReference<K, V> = KeyWeakReference(key, value, queue)

    override fun keyOf(ref: KeyWeakReference<K, V>): K = ref.key
}

class KeySoftReference<K, V>(val key: K, value: V, queue: ReferenceQueue<V>) : SoftReference<V>(value, queue)

class KeyWeakReference<K, V>(val key: K, value: V, queue: ReferenceQueue<V>) : WeakReference<V>(value, queue)