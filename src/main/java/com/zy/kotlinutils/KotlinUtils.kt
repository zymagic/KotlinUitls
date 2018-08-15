package com.zy.kotlinutils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.zy.kotlinutils.core.preference.ObjectTransfer
import com.zy.kotlinutils.core.preference.prefSupplier
import com.zy.kotlinutils.core.preference.transfer

fun init(context: Context) {
    AppInterface.app = context.applicationContext as Application
}

fun initPreference(t: ObjectTransfer, s: (String, Int) -> SharedPreferences) {
    transfer = t
    prefSupplier = s
}