package com.hashmob.aichat.data.interceptor

import android.content.Context
import com.hashmob.aichat.util.Connectivity
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        return if (Connectivity.checkInternetConnection(context)) chain.proceed(request) else throw NoInternetConnectionException(
            context
        )
    }
}