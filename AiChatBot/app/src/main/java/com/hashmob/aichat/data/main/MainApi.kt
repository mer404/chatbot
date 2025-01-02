package com.hashmob.aichat.data.main

import com.hashmob.aichat.data.model.apimodel.ChatWithAiResponse
import com.hashmob.aichat.data.model.apimodel.GenerateImageResponse
import com.hashmob.aichat.data.model.apimodel.WriteResponse
import io.reactivex.Observable
import retrofit2.http.*


interface MainApi {
    @POST(strPrefix + "images/generations")
    @JvmSuppressWildcards
    fun createImage(@Body user: Map<String, Any>): Observable<GenerateImageResponse>

    @POST(strPrefix + "chat/completions")
    @JvmSuppressWildcards
    fun aiChat(@Body user: Map<String, Any>): Observable<WriteResponse>

    @POST(strPrefix + "chat/completions")
    @JvmSuppressWildcards
    fun composition(@Body user: Map<String, Any>): Observable<ChatWithAiResponse>

    companion object {
        const val strPrefix = "/v1/"
    }

}