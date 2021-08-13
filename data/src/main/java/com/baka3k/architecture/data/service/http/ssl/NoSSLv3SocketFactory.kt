package com.baka3k.architecture.data.service.http.ssl

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory

class NoSSLv3SocketFactory : SSLSocketFactory {
    private val delegate: SSLSocketFactory

    constructor() {
        delegate = HttpsURLConnection.getDefaultSSLSocketFactory()
    }

    constructor(delegate: SSLSocketFactory) {
        this.delegate = delegate
    }

    override fun getDefaultCipherSuites(): Array<String?>? {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String?>? {
        return delegate.supportedCipherSuites
    }

    private fun makeSocketSafe(socket: Socket): Socket {
//        if (socket is SSLSocket) {
//            return NoSSLv3SSLSocket(socket)
//        }
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        return makeSocketSafe(delegate.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket {
        return makeSocketSafe(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        host: String,
        port: Int,
        localHost: InetAddress,
        localPort: Int
    ): Socket {
        return makeSocketSafe(delegate.createSocket(host, port, localHost, localPort))
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket {
        return makeSocketSafe(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int
    ): Socket {
        return makeSocketSafe(delegate.createSocket(address, port, localAddress, localPort))
    }
}