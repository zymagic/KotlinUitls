package com.zy.kotlinutils.core.cache

/**
 * Created by zy on 17-12-17.
 */
class FileCache<T>(private val dir: String, private val transfer: FileTransfer<T>) : AbsCache<String, T>() {

    override fun onPut(key: String, value: T) {
        transfer.saveToFile("$dir/$key", value)
    }

    override fun onGet(key: String): T? {
        return transfer.decode("$dir/$key")
    }

    override fun onRemove(key: String): T? {
        return null
    }
}

interface FileTransfer<T> {
    fun decode(file: String): T?
    fun saveToFile(file: String, obj: T)
}