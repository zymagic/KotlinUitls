package com.zy.kotlinutils.core.cache

/**
 * Created by zy on 17-12-8.
 */
abstract class AbsCache<K, V> : Cache<K, V> {
    var transformer: ((K) -> K)? = null

    private fun K.transform() = transformer?.invoke(this) ?: this

    override fun put(key: K, value: V) {
        onPut(key.transform(), value)
    }

    override fun get(key: K): V? {
        return onGet(key.transform())
    }

    override fun remove(key: K): V? {
        return onRemove(key.transform())
    }

    abstract fun onPut(key: K, value: V)
    abstract fun onGet(key: K): V?
    abstract fun onRemove(key: K): V?
}