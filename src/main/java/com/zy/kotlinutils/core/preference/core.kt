package com.zy.kotlinutils.core.preference

import android.content.SharedPreferences
import com.zy.kotlinutils.AppInterface
import com.zy.kotlinutils.core.ptr
import kotlin.reflect.KProperty0

var transfer: ObjectTransfer = object : ObjectTransfer {}

var prefSupplier: (String, Int) -> SharedPreferences = AppInterface.app::getSharedPreferences

fun <T> KProperty0<T>.deletePreference() {
    getDelegate().ptr<Preference<T>>()!!.delete(this)
}

fun <T> KProperty0<T>.exists() : Boolean {
    return getDelegate().ptr<Preference<T>>()!!.exists(this)
}