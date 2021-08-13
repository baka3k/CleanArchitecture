package com.baka3k.architecture.data.utils.connectivity


class NetworkStatus {
    private var mHasInternetConnection: Boolean = false

    fun hasInternetConnection(): Boolean {
        return mHasInternetConnection
    }

    fun setInternetConnectionState(hasInternetConnection: Boolean) {
        mHasInternetConnection = hasInternetConnection
    }
}
