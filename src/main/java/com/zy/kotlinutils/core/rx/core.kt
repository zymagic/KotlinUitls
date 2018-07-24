package com.zy.kotlinutils.core.rx

import kotlin.reflect.KClass
import kotlin.reflect.KType

interface Disposable {
    fun dispose()
}

interface ObservableSource<T> {
    fun subscribe(observer: Observer<T>)
}

interface Observer<T> {
    fun onSubscribe(d: Disposable?)
    fun onNext(t: T)
    fun onError(t: Throwable)
    fun onComplete()
}

interface Emitter<T> {
    fun onNext(t: T)
    fun onError(t: Throwable)
    fun onComplete()
}

fun <T, R> Observable<T>.map(mapFunction: T.() -> R) = MapObservable(this, mapFunction)

fun <T, R> Observable<T>.flatMap(f: (T) -> Observable<R>) = FlatMapObservable(this, f)

fun <T> Observable<T>.observeOn(scheduler: Scheduler) = ObserveOnObservable(this, scheduler)

fun <T> Observable<T>.subscribeOn(scheduler: Scheduler) = SubscribeOnObservable(this, scheduler)

fun <T> Observable<T>.delay(delay: Long) = DelayedObservable(this, delay)

fun <R> rxOf(f : () -> R) = CreatorObservable(f)

fun <T> rxBy(f : (Emitter<T>) -> Unit) = EmitterObservable(f)

fun <T> T.rx() = SingleObservable(this)

fun <T> Array<T>.rxBatch() = BatchObservable(::iterator)
fun <T> Iterable<T>.rxBatch() = BatchObservable(::iterator)

fun testRx() {
    var a = listOf(1, 2)
    rxOf {
        1
    }.map {
        toFloat()
    }.flatMap {
        it.rx()
    }.subscribeOn(IO)
            .observeOn(MAIN)
            .subscribe { 12345 }
}