package com.example.mercedesbenzapp.rest

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain.request().newBuilder().apply {
            addHeader("Authorization", "Bearer " +
                    "aH4ZLvbBoIbYUMcJqpFtMK9nMC7jav-ixh158vSTauJVi4xg5hmvLuukN3qt_5ERlvzF_NHM6CJ5GZZyBBSSRYJz4cXsC-J-ahmdjn6T7LOx937aG4whtMAjhSjtY3Yx")
        }.build().also { return chain.proceed(it) }
    }
}