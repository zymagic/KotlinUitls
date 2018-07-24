package com.zy.kotlinutils.core.rx

val EMPTY_DISPOSABLE = object : Disposable {
    override fun dispose() {
        // ignore
    }
}