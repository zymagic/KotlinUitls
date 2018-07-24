package com.zy.kotlinutils.core

/**
 * Created by zy on 18-1-2.
 */
private interface LifecycleSubject {
    fun onStart()
    fun onStop()
    fun onResume()
    fun onPause()
    fun onDestroy()
}

abstract class LifecycleObserver: LifecycleSubject

abstract class SimpleLifecycleObserver: LifecycleObserver() {
    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onDestroy() {
    }
}

class LifecycleObservable: LifecycleSubject {
    private val observers: MutableList<LifecycleObserver> = ArrayList()

    fun register(observer: LifecycleObserver) {
        observers.remove(observer)
        observers.add(observer)
    }

    fun unregister(observer: LifecycleObserver) {
        observers.remove(observer)
    }

    override fun onStart() {
        for (observer in observers) {
            observer.onStart()
        }
    }

    override fun onStop() {
        for (observer in observers) {
            observer.onStop()
        }
    }

    override fun onResume() {
        for (observer in observers) {
            observer.onResume()
        }
    }

    override fun onPause() {
        for (observer in observers) {
            observer.onPause()
        }
    }

    override fun onDestroy() {
        for (observer in observers) {
            observer.onDestroy()
        }
    }

}