package com.zy.kotlinutils.core.preference

import android.content.Context
import android.content.SharedPreferences
import com.zy.kotlinutils.core.ptr
import kotlin.reflect.KProperty

class Preference<T>(private val def: T? = null,
                    private val key: String? = null,
                    private val file: String = "settings",
                    private val mode: Int = Context.MODE_PRIVATE) {

    private val pref: SharedPreferences by lazy { prefSupplier(file, mode) }

    operator fun getValue(obj: Any?, property: KProperty<*>): T {
        val key = key ?: property.name.formatPreferenceKey()
        return when (property.javaClass) {
            Int::class.java -> pref.getInt(key, def.asNumber()?.toInt() ?: 0)
            Short::class.java -> pref.getInt(key, def.asNumber()?.toInt() ?: 0).toShort()
            Byte::class.java -> pref.getInt(key, def.asNumber()?.toInt() ?: 0).toByte()
            Long::class.java -> pref.getLong(key, def.asNumber()?.toLong() ?: 0L)
            Float::class.java -> pref.getFloat(key, def.asNumber()?.toFloat() ?: 0f)
            Boolean::class.java -> pref.getBoolean(key, def.ptr(false))
            String::class.java -> pref.getString(key, def.ptr())
            else -> transfer.parseFromString<T>(pref.getString(key, null)) ?: def
        } as T
    }

    operator fun setValue(obj: Any?, property: KProperty<*>, value: T) {
        val key = key ?: property.name.formatPreferenceKey()
        when (property.javaClass) {
            Int::class.java, Short::class.java, Byte::class.java -> {
                value.asNumber()?.let {
                    pref.edit().putInt(key, it.toInt()).apply()
                }
            }
            Long::class.java -> {
                value.asNumber()?.let {
                    pref.edit().putLong(key, it.toLong()).apply()
                }
            }
            Float::class.java -> {
                value.asNumber()?.let {
                    pref.edit().putFloat(key, it.toFloat()).apply()
                }
            }
            Boolean::class.java -> {
                value.ptr<Boolean>()?.let {
                    pref.edit().putBoolean(key, it).apply()
                }
            }
            String::class.java -> {
                pref.edit().putString(key, value.ptr<String>()).apply()
            }
            else -> {
                transfer.flattenToString(value)?.let {
                    pref.edit().putString(key, it).apply()
                }
            }
        }
    }

    fun delete(property: KProperty<*>) {
        pref.edit().remove(key ?: property.name.formatPreferenceKey()).apply()
    }

    fun exists(property: KProperty<*>) : Boolean = pref.contains(key ?: property.name.formatPreferenceKey())

    private fun String.formatPreferenceKey(): String {
        return fold(StringBuilder()) { sb, c ->
            if (c.isUpperCase()) {
                sb.append("_")
                sb.append(c.toLowerCase())
            } else {
                sb.append(c)
            }
        }.toString()
    }

    private fun <T> T.asNumber(): Number? = if (this is Number) this else null
}