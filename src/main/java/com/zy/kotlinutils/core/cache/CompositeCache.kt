package com.zy.kotlinutils.core.cache

/**
 * Created by zy on 17-12-10.
 */
open class CompositeCache<K, V>(vararg val caches: Cache<K, V>): Cache<K, V> {

    override fun put(key: K, value: V) {
        for (cache in caches) {
            cache.put(key, value)
        }
    }

    override fun get(key: K): V? {
        var index = 0
        var obj: V? = null
        for ((i, cache) in caches.withIndex()) {
            index = i
            obj = cache.get(key)
            if (obj != null) {
                break
            }
        }
        if (obj != null) {
            for (i in index - 1 downTo 0) {
                caches[i].put(key, obj)
            }
        }
        return obj
    }

    protected open fun onGet(key: K, obj: V, depth: Int) {}

    override fun remove(key: K): V? {
        var obj: V? = null
        for (cache in caches) {
            cache.remove(key)?.let { if (obj == null) obj = it }
        }
        return obj
    }

    override fun all(): Map<K, V> {
        for (cache in caches) {
            var map = cache.all()
            if (map.isNotEmpty()) {
                return map
            }
        }
        return HashMap()
    }

}