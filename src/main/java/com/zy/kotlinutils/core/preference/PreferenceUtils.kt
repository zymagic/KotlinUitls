package com.zy.kotlinutils.core.preference

import android.content.Context
import com.zy.kotlinutils.AppInterface

/**
 * Created by zy on 18-1-30.
 */
private val pf = AppInterface.app.getSharedPreferences("launcher", Context.MODE_PRIVATE)

fun intPreference(key: String, def: Int): Int {
    return pf.getInt(key, def)
}

fun setIntPreference(key: String, value: Int) {
    pf.edit().putInt(key, value).apply()
}

fun longPreference(key: String, def: Long): Long {
    return pf.getLong(key, def)
}

fun setLongPreference(key: String, value: Long) {
    pf.edit().putLong(key, value).apply()
}

fun floatPreference(key: String, def: Float): Float {
    return pf.getFloat(key, def)
}

fun setFloatPreference(key: String, value: Float) {
    pf.edit().putFloat(key, value).apply()
}

fun booleanPreference(key: String, def: Boolean): Boolean {
    return pf.getBoolean(key, def)
}

fun setBooleanPreference(key: String, value: Boolean) {
    pf.edit().putBoolean(key, value).apply()
}

fun stringPreference(key: String, def: String): String {
    return pf.getString(key, def)
}

fun setStringPreference(key: String, value: String) {
    pf.edit().putString(key, value).apply()
}

fun containsPreference(key: String): Boolean {
    return pf.contains(key)
}

infix fun Int.saveTo(key: String) = setIntPreference(key, this)
infix fun Long.saveTo(key: String) = setLongPreference(key, this)
infix fun Float.saveTo(key: String) = setFloatPreference(key, this)
infix fun Boolean.saveTo(key: String) = setBooleanPreference(key, this)
infix fun String.saveTo(key: String) = setStringPreference(key, this)

infix fun String.save(value: Int) = setIntPreference(this, value)
infix fun String.save(value: Long) = setLongPreference(this, value)
infix fun String.save(value: Float) = setFloatPreference(this, value)
infix fun String.save(value: Boolean) = setBooleanPreference(this, value)
infix fun String.save(value: String) = setStringPreference(this, value)

fun String.pfInt(def: Int): Int = intPreference(this, def)
fun String.pfLong(def: Long): Long = longPreference(this, def)
fun String.pfFloat(def: Float): Float = floatPreference(this, def)
fun String.pfBoolean(def: Boolean): Boolean = booleanPreference(this, def)
fun String.pfString(def: String): String = stringPreference(this, def)

fun Int.optPreference(key: String): Int = intPreference(key, this)
fun Long.optPreference(key: String): Long = longPreference(key, this)
fun Float.optPreference(key: String): Float = floatPreference(key, this)
fun Boolean.optPreference(key: String): Boolean = booleanPreference(key, this)
fun String.optPreference(key: String): String = stringPreference(key, this)

fun String.isInPreference() = containsPreference(this)

fun testPf() {

}