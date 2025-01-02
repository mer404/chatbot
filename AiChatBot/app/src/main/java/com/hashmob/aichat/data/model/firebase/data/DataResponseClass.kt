package com.hashmob.aichat.data.model.firebase.data

import com.google.gson.annotations.SerializedName

data class DataResponseClass(val data: Data? = null, val status: Int? = null)

data class PromptItem(val prompttext: String? = null, var flag: Boolean? = false)

data class StyleItem(
    val img_link: String? = null,
    val name: String? = null,
    var flag: Boolean? = false
)
data class Response(

    @SerializedName("data")
    val data: Data? = null,

    @SerializedName("app_user")
    val appUser: AppUser? = null,

    @SerializedName("Users")
    val users: Users? = null
)

class Users {
    val kay: String = ""
}
data class all_service(
    @SerializedName("usageCount")
    var usageCount: Int = 0


)
data class GenerateImageService(

    @SerializedName("firstTimeUsedTimestamp")
    var firstTimeUsedTimestamp: Double = 0.0,

    @SerializedName("throttle")
    var throttle: Int = 0
)

class AppUser {}

data class Data(
    val language: List<LanguageItem?>? = null,
    val style: List<StyleItem?>? = null,
    val prompt: List<PromptItem?>? = null
)

data class LanguageItem(
    val img_link: String? = null,
    val name: String? = null,
    var flag: Boolean? = false
)
