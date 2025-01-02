package com.hashmob.aichat.util

import android.util.Log
import com.hashmob.aichat.BuildConfig

object LogUtils {
    fun Print(tag: String?, text: String) {
        if (BuildConfig.DEBUG) Log.e(tag, "==========$text")
    }
}