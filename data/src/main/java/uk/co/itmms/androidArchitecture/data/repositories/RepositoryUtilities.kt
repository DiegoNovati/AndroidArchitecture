package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackend

/**
 * Wrapper to manage the case when the backend returns a 304 status (BackendException.NoDataChanges)
 *
 * defaultLambda  : default lambda to execute (backend -> database -> returned value
 */
suspend fun <T> invokeRepository(
    defaultLambda: suspend () -> Either<FailureRepositoryBackend, T>,
): Either<FailureRepositoryBackend, T>  where T : Any=
    try {
        defaultLambda.invoke()
    } catch (e: BackendException) {
        when (e.errorCode) {
            BackendErrorCode.UnknownHost,
            BackendErrorCode.Timeout,
            BackendErrorCode.SSLError,
            BackendErrorCode.HttpUnmanaged,
            BackendErrorCode.IO -> Either.Left(FailureRepositoryBackend.ConnectionError)

            BackendErrorCode.Http400,
            BackendErrorCode.Http401,
            BackendErrorCode.Http403,
            BackendErrorCode.Unexpected -> Either.Left(FailureRepositoryBackend.BackendError)
        }
    }