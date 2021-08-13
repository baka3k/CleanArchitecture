package com.baka3k.architecture.clean.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.baka3k.architecture.data.utils.connectivity.NetworkConnection
import com.baka3k.architecture.data.utils.connectivity.NetworkStatus

class NetworkConnectionImpl(private val connectivityManager: ConnectivityManager) :
    NetworkConnection {
    private val networkStatus: NetworkStatus = NetworkStatus()
    override fun netWorkStatus(): NetworkStatus {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val activeNetwork = connectivityManager.activeNetwork
            if (activeNetwork == null) {
                networkStatus.setInternetConnectionState(false)
            } else {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                if (networkCapabilities == null) {
                    networkStatus.setInternetConnectionState(false)
                } else {
                    val value =
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    networkStatus.setInternetConnectionState(value)
                }
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo
            if (nwInfo == null) {
                networkStatus.setInternetConnectionState(false)
            } else {
                networkStatus.setInternetConnectionState(nwInfo.isConnected)
            }
        }
        return networkStatus
    }
}