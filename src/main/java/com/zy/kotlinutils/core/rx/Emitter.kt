package com.zy.kotlinutils.core.rx

class ObserverEmitter<T>(private val observer: Observer<T>) : Emitter<T> {
    override fun onNext(t: T) {
        observer.onNext(t)
    }

    override fun onError(t: Throwable) {
        observer.onError(t)
    }

    override fun onComplete() {
        observer.onComplete()
    }
}