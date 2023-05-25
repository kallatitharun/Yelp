package com.example.yelp.data.remoteSource

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createOkHttpClient(): OkHttpClient {
    val timeout: Long = 100
    return OkHttpClient.Builder()
        .dispatcher(Dispatcher().apply {
            maxRequestsPerHost = 10
        })
        .addNetworkInterceptor(StethoInterceptor())
        .connectionPool(ConnectionPool(10, 5, TimeUnit.MINUTES))
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .build()
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    url: String,
    applyToBuilder: GsonBuilder.() -> Unit = {}
): T {
    return Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .apply {
                        applyToBuilder()
                    }
                    .create()
            )
        )
        .client(okHttpClient)
        .build().create(T::class.java)
}