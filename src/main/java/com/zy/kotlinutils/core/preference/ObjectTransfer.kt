package com.zy.kotlinutils.core.preference

interface ObjectTransfer {
    fun <T> flattenToString(obj: T): String? {
        return null
    }

    fun <T> parseFromString(str: String): T? {
        return null
    }
}