package com.baka3k.architecture.data.service.base

import com.baka3k.architecture.data.service.http.NetworkConnectionInterceptor
import com.baka3k.architecture.data.utils.connectivity.NetworkConnection
import okhttp3.CertificatePinner
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Service<T>(
    private val url: String,
    private val networkConnection: NetworkConnection,
    private val pinning: HashMap<String, String>?
) {
    companion object {
        private const val TIME_OUT = 30L//
    }

    fun create(className: Class<T>): T {
        val client = createClient(networkConnection)
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(className)
    }

    private fun createClient(networkConnection: NetworkConnection): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val pining = buildCertificatePinner()
        val spec: ConnectionSpec = buildConnectionSpec()
        return OkHttpClient.Builder()
            .certificatePinner(pining)
            .connectionSpecs(listOf(spec))
            .addInterceptor(logger)
            .addInterceptor(NetworkConnectionInterceptor(networkConnection))
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    private fun buildConnectionSpec() = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .tlsVersions(TlsVersion.TLS_1_2)
        .cipherSuites(
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
        )
        .build()

    private fun buildCertificatePinner(): CertificatePinner {
        return if (pinning != null && pinning.isNotEmpty()) {
            val builder = CertificatePinner.Builder()
            for (item in pinning) {
                builder.add(item.value, item.key)
            }
            builder.build()
        } else {
            CertificatePinner.Builder()
                .add("api.themoviedb.org", "sha256/+vqZVAzTqUP8BGkfl88yU7SQ3C8J2uNEa55B7RZjEg0=")
                .build() // default
        }
    }
}