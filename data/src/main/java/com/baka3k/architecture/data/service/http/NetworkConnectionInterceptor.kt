package com.baka3k.architecture.data.service.http

import com.baka3k.architecture.data.utils.connectivity.NetworkConnection
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkConnectionInterceptor(private val mNetworkConnection: NetworkConnection): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectivityException()
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
    fun isConnected(): Boolean {
        return mNetworkConnection.netWorkStatus().hasInternetConnection()
    }
}