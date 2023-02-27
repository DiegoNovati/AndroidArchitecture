package uk.co.itmms.androidArchitecture.data.datasources.network

enum class BackendErrorCode {
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

data class BackendException(
    val errorCode: BackendErrorCode,
    val errorMessage: String = "",
    val errorDisplay: String? = null
) : Exception(errorMessage)

class NotModifiedException : Exception()