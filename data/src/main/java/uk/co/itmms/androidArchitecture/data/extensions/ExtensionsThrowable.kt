package uk.co.itmms.androidArchitecture.data.extensions

import org.json.JSONObject
import retrofit2.HttpException
import uk.co.itmms.androidArchitecture.data.datasources.network.NetworkApiException
import uk.co.itmms.androidArchitecture.data.datasources.network.NetworkApiErrorCode
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException


fun Throwable.fullStackTraceToString(): String {
    StringWriter().use { stringWriter ->
        PrintWriter(stringWriter).use { printWriter ->
            this.printStackTrace(printWriter)
            return stringWriter.toString()
        }
    }
}

fun Throwable.toNetworkApiException(): NetworkApiException =
    when (this) {
        is UnknownHostException -> NetworkApiException(NetworkApiErrorCode.UnknownHost)
        is SocketTimeoutException -> NetworkApiException(NetworkApiErrorCode.Timeout)
        is SSLHandshakeException -> NetworkApiException(NetworkApiErrorCode.SSLError, this.localizedMessage ?: "")
        is SSLException -> NetworkApiException(NetworkApiErrorCode.SSLError, this.localizedMessage ?: "")
        is IOException -> {
            val errorMessage = this.message ?: ""
            if (errorMessage.contains("NotModifiedException"))
                NetworkApiException(NetworkApiErrorCode.NoDataChanges, "Data didn't change")
            else NetworkApiException(NetworkApiErrorCode.IO, errorMessage)
        }
        is HttpException -> {
            var errorMessage = ""
            var errorDisplay: String? = null
            this.response()?.errorBody()?.string()?.let {
                val jsonObject = JSONObject(it)
                errorMessage = jsonObject.get("error").toString()
                if (jsonObject.has("errorDisplay")) {
                    errorDisplay = jsonObject.get("errorDisplay").toString()
                }
            }
            val errorCode = when (this.code()) {
                400 -> NetworkApiErrorCode.Http400
                401 -> NetworkApiErrorCode.Http401
                else -> NetworkApiErrorCode.HttpUnmanaged
            }
            NetworkApiException(errorCode, errorMessage, errorDisplay)
        }
        else -> NetworkApiException(NetworkApiErrorCode.Unexpected, this.message ?: "")
    }