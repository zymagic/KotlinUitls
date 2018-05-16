package com.zy.kotlinutils.core.http

/**
 * Created by zy on 18-1-3.
 */
enum class Method(val desc: String) {
    GET("GET"), POST("POST")
}

const val CONTENT_TYPE_PLAIN_TEXT = "text"
const val CONTENT_TYPE_FILE = "file"
const val CONTENT_TYPE_IMAGE = "image"

class HttpParams {
    val params: MutableMap<String, MutableMap<String, String>> = HashMap()

    fun add(key: String, value: String): HttpParams {
        var map: MutableMap<String, String>? = params[CONTENT_TYPE_PLAIN_TEXT]
        if (map == null) {
            map = HashMap()
            params.put(CONTENT_TYPE_PLAIN_TEXT, map)
        }
        map.put(key, value)
        return this
    }

}

fun get(url: String) {

}

fun post(url: String) {

}

fun download(url: String) {

}

fun upload(url: String) {

}