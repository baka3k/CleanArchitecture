package com.baka3k.architecture.data.service.http.ssl

import android.util.Log
import javax.net.ssl.SSLSocket

class NoSSLv3SSLSocket constructor(delegate: SSLSocket) :
    DelegateSSLSocket(delegate) {
    override fun setEnabledProtocols(protocols: Array<String>) {
        var updatedProtocols = protocols
        if (updatedProtocols != null && updatedProtocols.size == 1 && "SSLv3" == updatedProtocols[0]) {
            val enabledProtocols = mutableListOf<String>()
            enabledProtocols.addAll(delegate.enabledProtocols)
            if (enabledProtocols.isNotEmpty()) {
                enabledProtocols.remove("SSLv3")
                Log.i("NoSSLv3SSLSocket", "Removed weak protocol from enabled protocols")
            } else {
                Log.i("NoSSLv3SSLSocket", "SSL stuck with protocol available for $enabledProtocols")
            }
            updatedProtocols = enabledProtocols.toTypedArray()
        }
        super.setEnabledProtocols(updatedProtocols)
    }
}