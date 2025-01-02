package com.hashmob.aichat.data.interceptor

import android.content.Context
import com.hashmob.aichat.R
import java.io.IOException

class NoInternetConnectionException internal constructor(private val context: Context) :
    IOException() {
    override val message: String
        get() = context.resources.getString(R.string.internet_error)
}