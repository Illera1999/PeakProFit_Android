package com.illera.peakprofit.core

import android.util.Log
import com.illera.peakprofit.BuildConfig

object Logger {
    fun d(tag: String, message: String) {
        if (BuildConfig.ALLOW_LOGS) Log.d(tag, message)
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.ALLOW_LOGS) Log.w(tag, message, throwable)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.ALLOW_LOGS) Log.e(tag, message, throwable)
    }
}
