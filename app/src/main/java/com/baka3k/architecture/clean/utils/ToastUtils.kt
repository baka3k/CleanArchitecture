package com.baka3k.architecture.clean.utils

import android.content.Context
import android.widget.Toast

/**
 * Toast util to prevent show toast many times, allow only last toast mess showing
 * */
object ToastUtils {
    private var toast: Toast? = null
    fun show(context: Context, idMess: Int, time: Int) {
        toast?.cancel() // cancel previous toast
        toast = Toast.makeText(context, idMess, time)
        toast?.show()
    }

    fun show(context: Context, mess: String, time: Int) {
        toast?.cancel() // cancel previous toast
        toast = Toast.makeText(context, mess, time)
        toast?.show()
    }
}