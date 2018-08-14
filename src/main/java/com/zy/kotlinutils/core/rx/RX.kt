package com.zy.kotlinutils.core.rx

fun <R> rxOf(f : () -> R) = CreatorObservable(f)

fun <T> rxBy(f : (Emitter<T>) -> Unit) = EmitterObservable(f)

fun <T> T.rx() = SingleObservable(this)

fun <T> Array<T>.rxBatch() = BatchObservable(::iterator)
fun <T> Iterable<T>.rxBatch() = BatchObservable(::iterator)