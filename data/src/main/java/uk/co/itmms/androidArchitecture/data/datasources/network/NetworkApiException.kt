package uk.co.itmms.androidArchitecture.data.datasources.network

enum class NetworkApiErrorCode {
    UnknownHost,
    Timeout,
    SSLError,
    Http400,
    Http401,
    Http403,
    HttpUnmanaged,
    NoDataChanges,
    IO,
    Unexpected,
}

data class NetworkApiException(
    val errorCode: NetworkApiErrorCode,
    val errorMessage: String = "",
    val errorDisplay: String? = null
) : Exception(errorMessage)

class NotModifiedException : Exception()