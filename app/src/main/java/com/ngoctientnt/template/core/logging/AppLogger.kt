package com.ngoctientnt.template.core.logging

import android.util.Log
import com.ngoctientnt.template.core.config.AppConfig

object AppLogger {

    fun d(tag: String, message: String) {
        if (AppConfig.isDebug) {
            Log.d(tag, message)
        }
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        if (AppConfig.isDebug) {
            if (throwable != null) Log.w(tag, message, throwable) else Log.w(tag, message)
        }
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (AppConfig.isDebug) {
            if (throwable != null) Log.e(tag, message, throwable) else Log.e(tag, message)
        }
    }
}
