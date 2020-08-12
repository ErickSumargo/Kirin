package com.bael.kirin.lib.network.base

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkInfo
import android.net.NetworkInfo.State.CONNECTED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ErickSumargo on 15/06/20.
 */

@Singleton
class Network @Inject constructor(@ApplicationContext context: Context) {
    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun networkInfo(type: Int): NetworkInfo.State {
        return connectivityManager.getNetworkInfo(type).state
    }

    private fun isNetworkConnected(networkType: Int): Boolean {
        return networkInfo(networkType) == CONNECTED
    }

    fun isConnected(): Boolean {
        return isNetworkConnected(TYPE_MOBILE) || isNetworkConnected(TYPE_WIFI)
    }
}
