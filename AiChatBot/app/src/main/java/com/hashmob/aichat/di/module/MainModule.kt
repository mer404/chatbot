package com.hashmob.aichat.di.module

import com.hashmob.aichat.data.main.MainApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal object MainModule {

    @Provides
    fun provideMainApi(retrofit: Retrofit): MainApi {
        return retrofit.create(MainApi::class.java)
    }
}