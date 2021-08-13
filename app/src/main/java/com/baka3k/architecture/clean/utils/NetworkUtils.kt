package com.baka3k.architecture.clean.utils

import android.content.Context
import android.net.ConnectivityManager
import com.baka3k.architecture.data.utils.connectivity.NetworkConnection

class NetworkUtils {
    companion object {
        fun getNetworkConnection(context: Context): NetworkConnection {
            val connectivityManager =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            return NetworkConnectionImpl(connectivityManager as ConnectivityManager)
        }
    }
}