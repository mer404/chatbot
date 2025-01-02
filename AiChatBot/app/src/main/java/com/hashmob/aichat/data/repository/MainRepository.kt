package com.hashmob.aichat.data.repository

import com.hashmob.aichat.data.main.MainApi
import com.hashmob.aichat.data.model.apimodel.ChatWithAiResponse
import com.hashmob.aichat.data.model.apimodel.GenerateImageResponse
import com.hashmob.aichat.data.model.apimodel.WriteResponse
import io.reactivex.Observable
import javax.inject.Inject

class MainRepository @Inject internal constructor(private val apiService: MainApi) {
    fun createImage(params: Map<String, Any>): Observable<GenerateImageResponse> {
        return apiService.createImage(params)
    }

    fun aiChat(params: Map<String, Any>): Observable<WriteResponse> {
        return apiService.aiChat(params)
    }

    fun composition(params: Map<String, Any>): Observable<ChatWithAiResponse> {
        return apiService.composition(params)
    }

}