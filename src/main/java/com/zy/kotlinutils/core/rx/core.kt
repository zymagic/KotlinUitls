package com.zy.kotlinutils.core.rx

fun <T, R> Observable<T>.map(mapFunction: T.() -> R) = MapObservable(this, mapFunction)

fun <T, R> Observable<T>.flatMap(f: (T) -> Observable<R>) = FlatMapObservable(this, f)

fun <T> Observable<T>.observeOn(scheduler: Scheduler) = ObserveOnObservable(this, scheduler)

fun <T> Observable<T>.subscribeOn(scheduler: Scheduler) = SubscribeOnObservable(this, scheduler)

fun <T> Observable<T>.delay(delay: Long) = DelayedObservable(this, delay)

fun <T, R> Observable<T>.retry(f: (Throwable) -> Observable<R>) = RetryObservable(this, f)

fun <T> Observable<T>.onSubscribe(f: (Disposable?) -> Unit) = OnSubscribeObservable(this, f)

fun <T> Observable<T>.onNext(f: (T) -> Unit) = OnNextObservable(this, f)

fun <T> Observable<T>.onError(f: (Throwable) -> Unit) = OnErrorObservable(this, f)

fun <T> Observable<T>.onComplete(f: () -> Unit) = OnCompleteObservable(this, f)

fun <T> Observable<T>.finally(f: () -> Unit) = FinallyObservable(this, f)

fun <T, R> Observable<T>.compose(f: (Observable<T>) -> Observable<R>) = f(this)

fun testRx() {
    var a = listOf(1, 2)
    rxOf {
        1
    }.map {
        toFloat()
    }.flatMap {
        it.rx()
    }.delay(1000)
    .retry {
        it.rx()
    }.onSubscribe {
        it?.dispose()
    }.onNext {
                it.toByte()
    }.onError {
        it.rx()
    }.onComplete {
        // xxx
    }.finally {
        // xxx
    }.subscribeOn(IO)
    .observeOn(MAIN)
    .compose {
        it.map { toByte() }
    }
    .subscribe { 12345 }
}