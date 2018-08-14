package com.zy.kotlinutils.core.rx

class LambdaObserver<T>(
        private val onSubscribe: (Disposable?) -> Unit,
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

class DelayObserver<T>(private val source: Observer<T>, private val delay: Long) : Observer<T> by source {

    override fun onNext(t: T) {
        WORK.execute(delay) { source.onNext(t) }
    }
}

class RetryObserver<T, R>(private val source: Observer<T>, private val origin: Observable<T>,
                          private val retry: (Throwable) -> Observable<R>) : Observer<T> by source {
    override fun onError(t: Throwable) {
        retry(t).flatMap { origin }.subscribe({}, source::onNext, source::onError, source::onComplete)
    }
}

class FinallyObserver<T>(private val source: Observer<T>, private val f: () -> Unit) : Observer<T> by source {

    override fun onError(t: Throwable) {
        try {
            source.onError(t)
        } finally {
            f()
        }
    }

    override fun onComplete() {
        try {
            source.onComplete()
        } finally {
            f()
        }
    }
}

class OnSubscribeObserver<T>(private val source: Observer<T>, private val f: (Disposable?) -> Unit) : Observer<T> by source {

    override fun onSubscribe(d: Disposable?) {
        source.onSubscribe(d)
        f(d)
    }

}

class OnNextObserver<T>(private val source: Observer<T>, private val f: (T) -> Unit) : Observer<T> by source {

    override fun onNext(t: T) {
        source.onNext(t)
        f(t)
    }

}

class OnErrorObserver<T>(private val source: Observer<T>, private val f: (Throwable) -> Unit) : Observer<T> by source {

    override fun onError(t: Throwable) {
        source.onError(t)
        f(t)
    }

}

class OnCompleteObserver<T>(private val source: Observer<T>, private val f: () -> Unit) : Observer<T> by source {

    override fun onComplete() {
        source.onComplete()
        f()
    }

}