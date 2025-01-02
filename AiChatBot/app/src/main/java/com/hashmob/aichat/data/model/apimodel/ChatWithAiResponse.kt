package com.hashmob.aichat.data.model.apimodel

import com.google.gson.annotations.SerializedName

data class ChatWithAiResponse(

    @field:SerializedName("choices")
    val choices: List<ChoicesItem?>? = null
)

data class ChoicesItem(

    @field:SerializedName("finish_reason")
    val finishReason: String? = null,

    @field:SerializedName("index")
    var index: Int? = null,

    @field:SerializedName("text")
    var text: String? = null,

    @field:SerializedName("isSend")
    val isSend: Boolean? = false,

    @field:SerializedName("logprobs")
    val logprobs: Any? = null,

    @field:SerializedName("isPlaying")
    var isPlaying: Boolean = false,
)
