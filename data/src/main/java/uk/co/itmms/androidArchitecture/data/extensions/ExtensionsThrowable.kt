package uk.co.itmms.androidArchitecture.data.extensions

import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
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

fun Throwable.toBackendException(): BackendException =
    when (this) {
        is UnknownHostException -> BackendException(BackendErrorCode.UnknownHost)
        is SocketTimeoutException -> BackendException(BackendErrorCode.Timeout)
        is SSLHandshakeException -> BackendException(BackendErrorCode.SSLError, this.localizedMessage ?: "")
        is SSLException -> BackendException(BackendErrorCode.SSLError, this.localizedMessage ?: "")
        is IOException -> {
            val errorMessage = this.message ?: ""
            if (errorMessage.contains("NotModifiedException"))
                BackendException(BackendErrorCode.NoDataChanges, "Data didn't change")
            else BackendException(BackendErrorCode.IO, errorMessage)
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
                400 -> BackendErrorCode.Http400
                401 -> BackendErrorCode.Http401
                else -> BackendErrorCode.HttpUnmanaged
            }
            BackendException(errorCode, errorMessage, errorDisplay)
        }
        else -> BackendException(BackendErrorCode.Unexpected, this.message ?: "")
    }