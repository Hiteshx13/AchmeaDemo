package com.achmea.demo.common

import android.content.Context
import android.net.ConnectivityManager

object Constants {
    const val BASE_URL = "https://cba.kooijmans.nl/"

    fun isInternetAvailable(context:Context): Boolean {
        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}