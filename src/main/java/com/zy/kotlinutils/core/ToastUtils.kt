package com.zy.kotlinutils.core

import android.widget.Toast
import com.zy.kotlinutils.AppInterface

/**
 * Created by zy on 17-12-27.
 */
fun toast(duration: Int = Toast.LENGTH_SHORT, txt: () -> String) {
    runInLooper {
        Toast.makeText(AppInterface.app, txt(), duration).show()
    }
}

fun String.toast(duration: Int = Toast.LENGTH_SHORT) {
    runInLooper {
        Toast.makeText(AppInterface.app, this, duration).show()
    }
}

fun Int.toast(duration: Int = Toast.LENGTH_SHORT) {
    runInLooper {
        Toast.makeText(AppInterface.app, this, duration).show()
    }
}

fun Throwable.toast(duration: Int = Toast.LENGTH_SHORT) {
    runInLooper {
        message?.let { Toast.makeText(AppInterface.app, message, duration).show() }
    }
}

fun testToast() {
    toast { "test" }
}