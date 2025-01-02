package com.hashmob.aichat.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object Connectivity {
    fun checkInternetConnection(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity == null) {
            return false
        } else {
            val info = connectivity.allNetworkInfo
            for (anInfo in info) {
                if (anInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }
}