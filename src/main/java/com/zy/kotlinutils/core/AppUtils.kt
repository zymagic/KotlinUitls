package com.zy.kotlinutils.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable

fun Context.load(intent: Intent, vararg params: Pair<String, Any>) {
    params.forEach {
        when (it.second) {
            is Number -> intent.putExtra(it.first, it.second as Number)
            is String -> intent.putExtra(it.first, it.second as String)
            is Char -> intent.putExtra(it.first, it.second as Char)
            is Parcelable -> intent.putExtra(it.first, it.second as Parcelable)
            is Serializable -> intent.putExtra(it.first, it.second as Serializable)
            else -> intent.putExtra(it.first, it.second.toString())
        }
    }
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

fun Context.startActivityCallback(intent: Intent, cbk: (Int, Intent?) -> Unit) {
    when(this) {
        is android.app.Activity -> {
            val fragment = ActionFragment()
            fragment.callback = cbk
            fragment.intent = intent
            safe {
                fragmentManager.beginTransaction().add(fragment, "cbk").commitAllowingStateLoss()
            }
        }
        is android.support.v4.app.Fragment -> {
            val fragment = ActionSupportFragment()
            fragment.callback = cbk
            fragment.intent = intent
            safe {
                fragmentManager?.beginTransaction()?.add(fragment, "cbk")?.commitAllowingStateLoss()
            }
        }
        else -> load(intent)
    }
}

internal const val REQUEST_CODE_CALLBACK = 23123

internal class ActionFragment : android.app.Fragment() {

    var intent : Intent? = null
    var callback: ((Int, Intent?) -> Unit)? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        startActivityForResult(intent, REQUEST_CODE_CALLBACK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CALLBACK) {
            callback?.invoke(resultCode, data)
        }
    }

    fun dismiss() {
        safe {
            fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
        }
    }
}

internal class ActionSupportFragment : android.support.v4.app.Fragment() {

    var intent : Intent? = null
    var callback: ((Int, Intent?) -> Unit)? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        startActivityForResult(intent, REQUEST_CODE_CALLBACK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CALLBACK) {
            callback?.invoke(resultCode, data)
        }
    }

    fun dismiss() {
        safe {
            fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
        }
    }
}