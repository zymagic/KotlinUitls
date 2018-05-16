package com.zy.kotlinutils.core.rx

import com.zy.kotlinutils.core.safe

/**
 * Created by zy on 18-1-17.
 */
fun <R> rx(f: () -> R) : Observable<R> {
    return Observable(f)
}

class Observable<T>(private var f: () -> T) {

    fun subscribe(observer: Observer<T>) {
        observer.onSubscribe()
        subscribe(observer::onNext, observer::onError)
        observer.onComplete()
    }

    fun subscribe(onNext: (T) -> Unit) {
        subscribe(onNext, null)
    }

    fun subscribe(onNext: (T) -> Unit, onError: ((Throwable) -> Unit)? = null) {
        safe {
            onNext(f())
        }.except {
            onError?.invoke(it)
        }
    }
}

class ObserverBuilder<T>(initial: (ObserverBuilder<T>) -> Unit) {
    var onSubscribe: (() -> Unit)? = null
    var onNext: ((T) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null
    var onComplete: (() -> Unit)? = null

    init {
        initial.invoke(this)
    }

    fun onSubscribe(f: () -> Unit) : ObserverBuilder<T> {
        onSubscribe = f
        return this
    }

    fun onNext(f: (T) -> Unit) : ObserverBuilder<T> {
        onNext = f
        return this
    }

    fun onError(f: (Throwable) -> Unit) : ObserverBuilder<T> {
        onError = f
        return this
    }

    fun onComplete(f: () -> Unit) : ObserverBuilder<T> {
        onComplete = f
        return this
    }

    fun build(): Observer<T> {
        var ret = object : Observer<T> {
            override fun onSubscribe() {
                onSubscribe?.invoke()
            }

            override fun onNext(t: T) {
                onNext?.invoke(t)
            }

            override fun onError(t: Throwable) {
                onError?.invoke(t)
            }

            override fun onComplete() {
                onComplete?.invoke()
            }

        }
        return ret
    }
}

interface Observer<T> {
    fun onSubscribe()
    fun onNext(t: T)
    fun onError(t: Throwable)
    fun onComplete()
}

fun rxTest() {
    rx {
        "string"
    }
}