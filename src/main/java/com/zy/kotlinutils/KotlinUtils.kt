package com.zy.kotlinutils

import android.app.Application
import android.content.Context

fun init(context: Context) {
    AppInterface.app = context.applicationContext as Application
}