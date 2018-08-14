package com.zy.kotlinutils.core

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog

/**
 * Created by zy on 17-12-27.
 */
fun Activity.alert(init: AlertDialog.Builder.() -> Unit): AlertDialog {
    return AlertDialog.Builder(this).apply(init).show()
}

fun AlertDialog.Builder.yes(txt: Int = android.R.string.ok, onClick: () -> Unit = {}): AlertDialog.Builder {
    if (txt != 0) {
        setPositiveButton(txt) { _, _ -> onClick() }
    }
    return this
}

fun AlertDialog.Builder.yes(txt: String, onClick: () -> Unit = {}): AlertDialog.Builder {
    setPositiveButton(txt) { _, _ -> onClick() }
    return this
}

fun AlertDialog.Builder.no(txt: String, onClick: () -> Unit = {}): AlertDialog.Builder {
    if (txt.isNotEmpty()) {
        setNegativeButton(txt) { _, _ -> onClick() }
    }
    return this
}

fun AlertDialog.Builder.no(txt: Int = android.R.string.cancel, onClick: () -> Unit = {}): AlertDialog.Builder {
    if (txt != 0) {
        setNegativeButton(txt) { _, _ -> onClick() }
    }
    return this
}

fun AlertDialog.Builder.onCancel(c: () -> Unit): AlertDialog.Builder {
    setOnCancelListener { c() }
    return this
}

fun AlertDialog.onDismiss(dismiss: () -> Unit): AlertDialog {
    setOnDismissListener { dismiss() }
    return this
}

fun AlertDialog.onCancel(dismiss: () -> Unit): AlertDialog {
    setOnCancelListener { dismiss() }
    return this
}

fun <R> Activity.progress(dialog: ProgressDialog? = null, run: () -> R): ProgressDialog {
    var pd = dialog ?: ProgressDialog(this)
    pd.show()
    async {
        run()
        uiThread {
            pd.dismiss()
        }
    }
    return pd
}

fun ttt() {

}

