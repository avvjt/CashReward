package com.aadevelopers.cashkingapp.helper

import android.content.Context
import android.os.Build
import android.widget.Toast

fun Context.isAndroid13() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun Context.showLongToast(message: String?) {
    this?.apply {
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }
}
fun Context.showShortToast(message: String?) {
    this?.apply {
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }
}