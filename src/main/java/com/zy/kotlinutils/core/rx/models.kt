package com.zy.kotlinutils.core.rx

interface ObservableSource<T> {
    fun subscribe(observer: Observer<T>)
}

abstract class Observable<T> : ObservableSource<T> {

    override fun subscribe(observer: Observer<T>) {
        subscribeActual(observer)
    }

    abstract fun subscribeActual(observer : Observer<T>)

    fun subscribe(onSubscribe: (Disposable?) -> Unit = {},
                  onNext: (T) -> Unit,
                  onError: (Throwable) -> Unit,
                  onComplete: () -> Unit = {}) : Disposable {
        return LambdaObserver(onSubscribe, onNext, onError, onComplete).apply { subscribeActual(this) }
    }
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

interface Disposable {
    fun dispose()
}

interface Scheduler {
    fun execute(delay: Long = 0, f: () -> Unit) : Int
    fun cancel(id: Int)
}

fun <T> Observable<T>.subscribe(onNext: (T) -> Unit = {}) : Disposable {
    return subscribe(onNext) {}
}

fun <T> Observable<T>.subscribe(onNext: (T) -> Unit, onError: (Throwable) -> Unit) : Disposable {
    return LambdaObserver({}, onNext, onError, {}).apply { subscribeActual(this) }
}