package com.zy.kotlinutils.core.component

import android.app.Activity
import com.zy.kotlinutils.core.LifecycleObservable

/**
 * Created by zy on 18-1-2.
 */
open class LifecycleActivity : Activity() {
    val lifecycle: LifecycleObservable = LifecycleObservable()

    override fun onStart() {
        super.onStart()
        lifecycle.onStart()
    }

    override fun onStop() {
        super.onStop()
        lifecycle.onStop()
    }

    override fun onResume() {
        super.onResume()
        lifecycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        lifecycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.onDestroy()
    }
}