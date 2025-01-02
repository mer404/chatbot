package com.hashmob.aichat.di.module

import android.app.Application
import android.content.Context
import com.hashmob.aichat.BuildConfig
import com.hashmob.aichat.data.interceptor.HeaderInterceptor
import com.hashmob.aichat.data.interceptor.NetworkConnectionInterceptor
import com.hashmob.aichat.util.Preferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton



@Module
class AppModule {
    @Singleton
    @Provides
    fun provideAppContext(context: Application): Context {
        return context
    }

    @Singleton
    @Provides
    fun providePreferences(context: Application): Preferences {
        return Preferences(context)
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(context: Application): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.readTimeout(60, TimeUnit.SECONDS)
        client.connectTimeout(60, TimeUnit.SECONDS)
        client.addInterceptor(NetworkConnectionInterceptor(context))
        client.addInterceptor(HeaderInterceptor(context))
        client.addInterceptor(loggingInterceptor)
        return Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }


}