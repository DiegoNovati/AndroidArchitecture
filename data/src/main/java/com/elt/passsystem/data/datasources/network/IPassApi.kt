package com.elt.passsystem.data.datasources.network

import android.content.Context
import com.elt.passsystem.data.BuildConfig
import com.elt.passsystem.data.models.NetAuthenticateResponse
import com.elt.passsystem.data.models.NetBookingsResponse
import com.elt.passsystem.data.models.NetCustomersResponse
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

fun createPassApi(context: Context, releaseMode: Boolean): IPassApi =
    getRetrofit(context, BuildConfig.BACKEND_URL, releaseMode).create(IPassApi::class.java)

private val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    .create()

internal fun getRetrofit(context: Context, url: String, releaseMode: Boolean): Retrofit =
    Retrofit
        .Builder()
        .baseUrl(url)
        .client(getHttpClient(context, releaseMode))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

private fun getHttpClient(context: Context, releaseMode: Boolean): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = if (releaseMode) HttpLoggingInterceptor.Level.NONE else HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .cache(Cache(context.cacheDir, (1024*1024*50).toLong()))
        .addNetworkInterceptor(CustomHttp304CodeInterceptor())
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

interface IPassApi {

    @POST("/api/v1/authenticate")
    suspend fun authenticate(
        @Header(PLATFORM) platform: String = PLATFORM_VALUE,
        @Header(APP_VERSION_NAME) appVersionName: String = VERSION_NAME,
        @Header(AUTHORISATION) auth: String,
        @Header(UNIQUE_DEVICE_ID) uniqueDeviceId: String = UNIQUE_DEVICE_ID_VALUE
    ): NetAuthenticateResponse

    @GET("/api/v3/offices/{$OFFICE_BID}/customers")
    suspend fun getCustomerList(
        @Header(AUTHORISATION) auth: String,
        @Path(OFFICE_BID) officeBid: String
    ): NetCustomersResponse

    @GET("api/v1/offices/{$OFFICE_BID}/bookings")
    suspend fun getBookingList(
        @Header(AUTHORISATION) authorization: String,
        @Path(OFFICE_BID) officeBid: String
    ): NetBookingsResponse

    companion object {
        const val PLATFORM = "Platform"
        const val PLATFORM_VALUE = "Android"
        const val APP_VERSION_NAME = "App-Version-Name"
        const val VERSION_NAME = "1.83.0"
        const val AUTHORISATION = "Authorization"
        const val UNIQUE_DEVICE_ID = "Unique-Device-Identifier"
        const val UNIQUE_DEVICE_ID_VALUE = "511631b02984faf0"
        internal const val OFFICE_BID = "officeBid"
    }
}