package com.zy.kotlinutils.core.net.http

import java.lang.reflect.Proxy
import kotlin.reflect.KClass

fun <T: Any> build(t: KClass<T>): T {
    return Proxy.newProxyInstance(t.java.classLoader, arrayOf(t.java)) { proxy, method, args ->

    } as T
}