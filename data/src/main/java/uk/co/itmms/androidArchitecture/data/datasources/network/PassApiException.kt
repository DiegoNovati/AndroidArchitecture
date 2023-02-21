package uk.co.itmms.androidArchitecture.data.datasources.network

enum class PassApiErrorCode {
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

data class PassApiException(
    val errorCode: PassApiErrorCode,
    val errorMessage: String = "",
    val errorDisplay: String? = null
) : Exception(errorMessage)

class NotModifiedException : Exception()