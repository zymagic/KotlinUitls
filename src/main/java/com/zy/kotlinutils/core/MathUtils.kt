package com.zy.kotlinutils.core

import kotlin.math.absoluteValue

/**
 * Created by zy on 18-1-31.
 */
fun <R : Comparable<R>> R.clamp(min: R, max: R) : R {
    return when {
        this < min -> min
        this > max -> max
        else -> this
    }
}

infix fun <T : Comparable<T>> T.clamp(pair: Pair<T, T>) : T {
    return when {
        this < pair.first -> pair.first
        this > pair.second -> pair.second
        else -> this
    }
}

fun <T: Comparable<T>> clamp(min: T, max: T, block: () -> T): T {
    return block().clamp(min, max)
}

infix fun <T: Comparable<T>> Pair<T, T>.clamp(block: () -> T) : T {
    return block().clamp(first, second)
}

infix fun <T : Comparable<T>> T.minFor(block: () -> T) : T {
    return block().minTo(this)
}

infix fun <T : Comparable<T>> T.minTo(other: T) : T {
    return if (this < other) other else this
}

infix fun <T : Comparable<T>> T.maxFor(block: () -> T) : T {
    return block().maxTo(this)
}

infix fun <T : Comparable<T>> T.maxTo(other: T) : T {
    return if (this > other) other else this
}

fun positive(block: () -> Int) = block().minTo(0)

fun negative(block: () -> Int) = block().maxTo(0)
fun negative(block: () -> Float) = block().maxTo(0f)
fun negative(block: () -> Long) = block().maxTo(0L)
fun negative(block: () -> Double) = block().maxTo(0.0)

infix fun Pair<Int, Int>.mix(a: Float) : Int {
    return (first + (second - first) * a).toInt()
}

infix fun Float.mix(a: Pair<Int, Int>): Int {
    return a.mix(this)
}

fun Float.mix(a: Int, b: Int): Int {
    return (a + (b - a) * this).toInt()
}

fun hypot(vararg a : Float) : Float {
    return a.fold(0f) {
        acc, v -> acc + v * v
    }.sqrt()
}

fun hypot(vararg a : Int) : Float {
    return a.fold(0) {
        acc, v -> acc + v * v
    }.sqrt()
}

fun Int.sqrt() = kotlin.math.sqrt(absoluteValue.toFloat())
fun Float.sqrt() = kotlin.math.sqrt(absoluteValue)
fun Long.sqrt() = kotlin.math.sqrt(absoluteValue.toDouble())
fun Double.sqrt() = kotlin.math.sqrt(absoluteValue)

fun testMath() {
    1 to 2 mix 3.0f
    3.0f mix (1 to 2)
}