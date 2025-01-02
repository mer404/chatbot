package com.hashmob.aichat.data.interceptor

import android.content.Context
import android.os.Build
import com.hashmob.aichat.BuildConfig
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.main.RestClient
import com.hashmob.aichat.util.Preferences
import com.hashmob.aichat.util.Utils
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class HeaderInterceptor(var context: Context) : Interceptor {


    private lateinit var preferences: Preferences


    //API
    private val compositeDisposable = CompositeDisposable()


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        preferences = Preferences(context)
        val original: Request = chain.request()
        val builder: Request.Builder = original.newBuilder()
            .header("User-Agent", RestClient.USER_AGENT)
            .header("App-Secret", RestClient.APP_SECRET)
            .header("App-Track-Version", RestClient.VERSION_NAME)
            .header("App-Device-Type", RestClient.DEVICE_TYPE_ANDROID)
            .header("App-Store-Version", BuildConfig.VERSION_NAME)
            .header("App-Store-Version", BuildConfig.VERSION_NAME)
            .header("App-Device-Model", Utils.deviceName)
//            .header("Authorization", Utils.base64Decoded(preferences.getRemoteConfig()?.Authorization.toString()))
            .header("Authorization", "Bearer sk-proj-XUvkIzao93Vlws2L4NoKT3BlbkFJqEnMiag7FiqvOi2kW74d")
            .header("App-Os-Version", "" + Build.VERSION.SDK_INT)
            .header("App-Store-Build-Number", "" + BuildConfig.VERSION_CODE)
            .header("Content-Type", RestClient.CONTENT_TYPE)
            .header("Auth-Token", preferences.getString(Constants.auth_token)!!)
            .header("Access-Token", preferences.getString(Constants.access_token)!!)
            .method(original.method, original.body)
        val request: Request = builder.build()
        return chain.proceed(request)
    }

    private val TAG = "HeaderInterceptor"


}