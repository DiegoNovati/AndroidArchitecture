package uk.co.itmms.androidArchitecture.data.extensions

import uk.co.itmms.androidArchitecture.data.datasources.network.PassApiErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.PassApiException
import org.json.JSONObject
import retrofit2.HttpException
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

fun Throwable.toPassApiException(): PassApiException =
    when (this) {
        is UnknownHostException -> PassApiException(PassApiErrorCode.UnknownHost)
        is SocketTimeoutException -> PassApiException(PassApiErrorCode.Timeout)
        is SSLHandshakeException -> PassApiException(PassApiErrorCode.SSLError, this.localizedMessage ?: "")
        is SSLException -> PassApiException(PassApiErrorCode.SSLError, this.localizedMessage ?: "")
        is IOException -> {
            val errorMessage = this.message ?: ""
            if (errorMessage.contains("NotModifiedException"))
                PassApiException(PassApiErrorCode.NoDataChanges, "Data didn't change")
            else PassApiException(PassApiErrorCode.IO, errorMessage)
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
                400 -> PassApiErrorCode.Http400
                401 -> PassApiErrorCode.Http401
                else -> PassApiErrorCode.HttpUnmanaged
            }
            PassApiException(errorCode, errorMessage, errorDisplay)
        }
        else -> PassApiException(PassApiErrorCode.Unexpected, this.message ?: "")
    }