package com.zy.kotlinutils.core.rx

class LambdaObserver<T>(
            private val onSubscribe : (Disposable?) -> Unit,
            private val onNext: (T) -> Unit,
            private val onError: (Throwable) -> Unit,
            private val onComplete: () -> Unit
        ) : Observer<T>, Disposable {

    override fun onSubscribe(d: Disposable?) {
        this.onSubscribe.invoke(d)
    }

    override fun onNext(t: T) {
        this.onNext.invoke(t)
    }

    override fun onError(t: Throwable) {
        this.onError.invoke(t)
    }

    override fun onComplete() {
        this.onComplete.invoke()
    }

    override fun dispose() {
    }
}

class MapObserver<T, R>(private val source: Observer<R>, private val map: (T) -> R) : Observer<T> {

    override fun onSubscribe(d: Disposable?) {
        source.onSubscribe(d)
    }

    override fun onNext(t: T) {
        source.onNext(map.invoke(t))
    }

    override fun onError(t: Throwable) {
        source.onError(t)
    }

    override fun onComplete() {
        source.onComplete()
    }

}

class FlatMapObserver<T, R>(private val source: Observer<R>, private val f: (T) -> Observable<R>) : Observer<T> {

    override fun onSubscribe(d: Disposable?) {
        source.onSubscribe(d)
    }

    override fun onNext(t: T) {
        f(t).subscribe({}, source::onNext, source::onError, source::onComplete)
    }

    override fun onError(t: Throwable) {
        source.onError(t)
    }

    override fun onComplete() {
        source.onComplete()
    }
}

class ObserveOnObserver<T>(private val source: Observer<T>, private val scheduler: Scheduler) : Observer<T> {

    override fun onSubscribe(d: Disposable?) {
        source.onSubscribe(d)
    }

    override fun onNext(t: T) {
        scheduler.execute { source.onNext(t) }
    }

    override fun onError(t: Throwable) {
        scheduler.execute { source.onError(t) }
    }

    override fun onComplete() {
        scheduler.execute { source.onComplete() }
    }
}