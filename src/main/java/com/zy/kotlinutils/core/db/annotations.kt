package com.zy.kotlinutils.core.db

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.lang.reflect.Field
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Table(val name: String)

@Target(AnnotationTarget.FIELD)
annotation class PrimaryKey

@Target(AnnotationTarget.FIELD)
annotation class NotNull

@Target(AnnotationTarget.FIELD)
annotation class Column(
        val name: String,
        val primary: Boolean = false,
        val notNull: Boolean = false
)

fun createTable(cls: KClass<*>) : String {
    val tableAnnotation = cls.java.annotations.find { it is Table }!! as Table
    val table = tableAnnotation.name
    var hasPrimaryKey = false
    val properties = cls.java.fields
            .map {
                val ca = it.annotations.find { it is Column }?.let { it as Column }
                val pa = it.annotations.find { it is PrimaryKey }?.let { it as PrimaryKey }
                val na = it.annotations.find { it is NotNull }?.let { it as NotNull }
                val name = ca?.name ?: it.name
                val pk = pa != null || ca != null && ca.primary
                val nn = !pk && (na != null || ca != null && ca.notNull)
                hasPrimaryKey = hasPrimaryKey || pk
                "$name ${columnType(it)}${ if (pk) " PRIMARY KEY" else ""}${ if (nn) " NOT NULL" else ""}${ if (pk) "" else defaultValue(it)}"
            }
    return "CREATE TABLE IF NOT EXISTS $table (${ if (hasPrimaryKey) "" else "_id PRIMARY KEY,"}${properties.joinToString(separator = ",")})"
}

fun dropTable(cls: KClass<*>) : String {
    val table = cls.java.annotations.find { it is Table }!! as Table
    return "DROP TABLE IF EXISTS ${table.name}"
}

fun columnType(p: Field): String {
    return when(p.type) {
        Int::class.java, Long::class.java, Boolean::class.java -> "INTEGER"
        Bitmap::class.java -> "BLOB"
        else -> "TEXT"
    }
}

fun defaultValue(p: Field): String {
    val o = p.declaringClass.newInstance()
    return when(p.type) {
        Int::class.java -> (p.get(o) as Int).let { if (it == 0) "" else " DEFAULT $it" }
        Long::class.java -> (p.get(o) as Long).let { if (it.toInt() == 0) "" else " DEFAULT $it"}
        Boolean::class.java -> if (p.get(o) as Boolean) " DEFAULT 1" else ""
        String::class.java -> p.get(o) as String
        else -> ""
    }
}

inline fun <reified T> fromCursor(cursor: Cursor): T {
    T::class.java.annotations.find { it is Table }!!
    val o = T::class.java.newInstance()
    T::class.java.fields.withIndex().forEach {
        when(it.value.type) {
            Int::class.java -> it.value.setInt(o, cursor.getInt(it.index))
            Long::class.java -> it.value.setLong(o, cursor.getLong(it.index))
            Boolean::class.java -> it.value.setBoolean(o, cursor.getInt(it.index) == 1)
            String::class.java -> it.value.set(o, cursor.getString(it.index))
            Bitmap::class.java -> it.value.set(o, decodeBitmap(cursor.getBlob(it.index)))
            Intent::class.java -> it.value.set(o, Intent.parseUri(cursor.getString(it.index), 0))
        }
    }
    return o
}

inline fun <reified T> Cursor.parseObject() : T = fromCursor(this)

fun decodeBitmap(ba: ByteArray) : Bitmap = BitmapFactory.decodeByteArray(ba, 0, ba.size)

class Favorites {

}