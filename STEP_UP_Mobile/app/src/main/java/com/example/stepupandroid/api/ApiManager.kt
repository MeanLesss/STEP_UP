package com.example.stepupandroid.api

import android.annotation.SuppressLint
import android.util.Log
import com.example.stepupandroid.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

open class ApiManager {
    private val mServerUrl = getBaseServerUrl()
    lateinit var mAllService: ServiceAPI

    init {
        initServices(initRetrofit())
    }

    private fun getBaseServerUrl(): String {
        return BuildConfig.BASE_URL
    }

    //INIT ALL PROFIT OBJECT
    private fun initServices(retrofit: Retrofit) {
        mAllService = retrofit.create(ServiceAPI::class.java)
    }

    private fun initRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("DEV_HTTP_LOG", message) // Add additional debug logging
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


        val client = OkHttpClient.Builder().apply {
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts: Array<TrustManager> = arrayOf(
                    object : X509TrustManager {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) {

                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) {

                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

                // Install the all-trusting trust manager
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
                this.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                this.hostnameVerifier { _, _ -> true }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            networkInterceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            })
            addInterceptor(loggingInterceptor)
            readTimeout(120, TimeUnit.SECONDS) // Adjust as needed
            connectTimeout(120, TimeUnit.SECONDS) // Adjust as needed
        }

        return Retrofit.Builder().baseUrl(mServerUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }

    enum class NetworkErrorStatus {
        UnProcessableEntity,//422
        ON_UNKNOWN_ERROR,
        ON_ERROR,
        UNAUTHORIZED,//401
        ON_TIMEOUT,
        ON_NETWORK_ERROR,
        NOT_FOUND//404
    }
}
