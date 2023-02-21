package uk.co.itmms.androidArchitecture.data.datasources.network

import okhttp3.Interceptor
import okhttp3.Response

class CustomHttp304CodeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        if (response.code == 304) {
            throw NotModifiedException()
        }

        return response
    }
}