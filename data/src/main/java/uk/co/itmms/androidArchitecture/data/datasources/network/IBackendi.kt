package uk.co.itmms.androidArchitecture.data.datasources.network

import uk.co.itmms.androidArchitecture.data.BuildConfig
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uk.co.itmms.androidArchitecture.data.models.NetAuthLoginRequest
import uk.co.itmms.androidArchitecture.data.models.NetAuthLoginResponse
import uk.co.itmms.androidArchitecture.data.models.NetProductsResponse
import uk.co.itmms.androidArchitecture.data.models.NetTodosResponse
import java.util.concurrent.TimeUnit

fun createBackend(releaseMode: Boolean, networkFlipperPlugin: NetworkFlipperPlugin? = null): IBackend =
    getRetrofit(BuildConfig.BACKEND_URL, releaseMode, networkFlipperPlugin).create(IBackend::class.java)

private val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    .create()

internal fun getRetrofit(url: String, releaseMode: Boolean, networkFlipperPlugin: NetworkFlipperPlugin? = null): Retrofit =
    Retrofit
        .Builder()
        .baseUrl(url)
        .client(getHttpClient(releaseMode, networkFlipperPlugin))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

private fun getHttpClient(releaseMode: Boolean, networkFlipperPlugin: NetworkFlipperPlugin? = null): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = if (releaseMode) HttpLoggingInterceptor.Level.NONE else HttpLoggingInterceptor.Level.BODY

    val builder = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        //.cache(Cache(context.cacheDir, (1024*1024*50).toLong()))
        //.addNetworkInterceptor(CustomHttp304CodeInterceptor())
        .addInterceptor(httpLoggingInterceptor)

    networkFlipperPlugin?.let {
        builder.addInterceptor(FlipperOkhttpInterceptor(it))
    }

    return builder.build()
}

interface IBackend {

    @POST("/auth/login")
    suspend fun login(
        @Body loginRequest: NetAuthLoginRequest,
    ): NetAuthLoginResponse

    @GET("/products")
    suspend fun getProducts(): NetProductsResponse

    @GET("/todos")
    suspend fun getTodos(): NetTodosResponse
}