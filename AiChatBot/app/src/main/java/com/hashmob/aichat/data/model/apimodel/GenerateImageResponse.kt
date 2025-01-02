package com.hashmob.aichat.data.model.apimodel

import com.google.gson.annotations.SerializedName

data class GenerateImageResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("created")
	val created: Int? = null
)

data class DataItem(

	@field:SerializedName("url")
	val url: String? = null
)
