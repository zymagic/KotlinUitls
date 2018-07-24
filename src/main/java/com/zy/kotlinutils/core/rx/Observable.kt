package com.zy.kotlinutils.core.rx

import java.util.concurrent.TimeUnit

abstract class Observable<T> : ObservableSource<T> {

    override fun subscribe(observer: Observer<T>) {
        subscribeActual(observer)
    }

    abstract fun subscribeActual(observer : Observer<T>)

    fun subscribe(onNext: (T) -> Unit = {}) : Disposable {
        return subscribe(onNext, {})
    }

    fun subscribe(onNext: (T) -> Unit, onError: (Throwable) -> Unit) : Disposable {
        return LambdaObserver({}, onNext, onError, {}).apply { subscribeActual(this) }
    }

    fun subscribe(onSubscribe: (Disposable?) -> Unit = {},
                  onNext: (T) -> Unit,
                  onError: (Throwable) -> Unit,
                  onComplete: () -> Unit = {}) : Disposable {
        return LambdaObserver(onSubscribe, onNext, onError, onComplete).apply { subscribeActual(this) }
    }
}

abstract class ObjectObservable<T> : Observable<T>() {
    override fun subscribeActual(observer: Observer<T>) {
        observer.onSubscribe(EMPTY_DISPOSABLE)
        try {
            doSubscribeActual(observer)
            observer.onComplete()
        } catch (e : Throwable) {
            observer.onError(e)
        }
    }

    abstract fun doSubscribeActual(observer: Observer<T>)
}

class SingleObservable<T>(private val t : T) : ObjectObservable<T>() {

    override fun doSubscribeActual(observer: Observer<T>) {
        observer.onNext(t)
    }

}

class BatchObservable<T>(private val source: () -> Iterator<T>) : Observable<T>() {

    override fun subscribeActual(observer: Observer<T>) {
        source.invoke().forEach {
            observer.onNext(it)
        }
    }
}

class MapObservable<T, R>(private val source: Observable<T>, private val f: (T) -> R) : Observable<R>() {

    override fun subscribeActual(observer: Observer<R>) {
        source.subscribeActual(MapObserver(observer, f))
    }
}

class FlatMapObservable<T, R>(private val source: Observable<T>, private val f : (T) -> Observable<R>) : Observable<R>() {

    override fun subscribeActual(observer: Observer<R>) {
        source.subscribe(FlatMapObserver(observer, f))
    }

}

class ObserveOnObservable<T>(private val source: Observable<T>, private val scheduler: Scheduler) : Observable<T>() {
    override fun subscribeActual(observer: Observer<T>) {
        source.subscribe(ObserveOnObserver(observer, scheduler))
    }
}

class SubscribeOnObservable<T>(private val source: Observable<T>, private val scheduler: Scheduler) : Observable<T>(), Disposable {

    var id : Int = 0

    override fun subscribeActual(observer: Observer<T>) {
        id = scheduler.execute { source.subscribe(observer) }
    }

    override fun dispose() {
        if (id != 0) {
            scheduler.cancel(id)
        }
    }
}

class CreatorObservable<T>(private val f: () -> T) : ObjectObservable<T>() {
    override fun doSubscribeActual(observer: Observer<T>) {
        observer.onNext(f.invoke())
    }
}

class EmitterObservable<T>(private val f: (Emitter<T>) -> Unit) : Observable<T>() {
    override fun subscribeActual(observer: Observer<T>) {
        observer.onSubscribe(EMPTY_DISPOSABLE)
        try {
            f(ObserverEmitter(observer))
        } catch (t: Throwable) {
            observer.onError(t)
        }
    }

}

class DelayedObservable<T>(private val source: Observable<T>, private val delay: Long) : Observable<T>(), Disposable {
    var id : Int = 0

    override fun subscribeActual(observer: Observer<T>) {
        id = WORK.execute(delay) { source.subscribe(observer) }
    }

    override fun dispose() {
        if (id != 0) {
            WORK.cancel(id)
        }
    }

}