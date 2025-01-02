package com.hashmob.aichat.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.model.firebase.data.Data
import com.hashmob.aichat.data.model.firebase.data.GenerateImageService
import com.hashmob.aichat.data.model.firebase.data.all_service
import com.hashmob.aichat.data.model.firebase.remoteconfig.RemoteConfigData

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class Preferences(context: Context?) {
    val sharedPreferences: SharedPreferences
    val editor: SharedPreferences.Editor


    fun isProVersion(): Boolean {
        return getInt(Constants.isProVersion) == Constants.pro_version
    }

    fun getInt(key: String?): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun putInt(key: String?, value: Int?) {
        editor.putInt(key, value!!)
        editor.commit()
    }

    fun getString(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun putString(key: String?, value: String?) {
        editor.putString(key, value)
        editor.commit()
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun putBoolean(key: String?, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }
    fun getLong(key: String?): Long {
        return sharedPreferences.getLong(key,0L)
    }

    fun putLong(key: String?, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    fun remove(key: String?) {
        editor.remove(key)
        editor.commit()
    }

    fun clear() {
        editor.clear().apply()
    }

    fun getRemoteConfig(): RemoteConfigData? {
        val strData = sharedPreferences.getString(Constants.remote_data, null)
        return if (strData == null || strData == "") RemoteConfigData() else Gson().fromJson(
            strData, RemoteConfigData::class.java
        )
    }

    fun setRemoteConfig(data: RemoteConfigData?) {
        val gson = Gson()
        val json = gson.toJson(data)
        editor.putString(Constants.remote_data, json)
        editor.commit()
    }

    fun setData(list: Data) {
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(Constants.appData, json)
        editor.commit()
    }

    fun getData(): Data {
        var arrayItems = Data()
        val serializedObject = sharedPreferences.getString(Constants.appData, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<Data>() {}.type
            arrayItems = gson.fromJson(serializedObject, type)
        }
        return arrayItems
    }
    fun setAllServiceData(list: all_service) {
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(Constants.allService, json)
        editor.commit()
    }

    fun getAllServiceData(): all_service {
        var arrayItems = all_service()
        val serializedObject = sharedPreferences.getString(Constants.allService, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<all_service>() {}.type
            arrayItems = gson.fromJson(serializedObject, type)
        }
        return arrayItems
    }
    fun setGenerateImageServiceData(list: GenerateImageService) {
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(Constants.generateImageService, json)
        editor.commit()
    }

    fun getGenerateImageServiceData(): GenerateImageService {
        var arrayItems = GenerateImageService()
        val serializedObject = sharedPreferences.getString(Constants.generateImageService, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<GenerateImageService>() {}.type
            arrayItems = gson.fromJson(serializedObject, type)
        }
        return arrayItems
    }

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = sharedPreferences.edit()
    }
}