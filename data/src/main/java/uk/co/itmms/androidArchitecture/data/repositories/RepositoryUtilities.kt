package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.data.datasources.network.NetworkApiErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.NetworkApiException
import uk.co.itmms.androidArchitecture.domain.repositories.RepositoryBackendFailure

/**
 * Wrapper to manage the case when the backend returns a 304 status (PassApiErrorCode.NoDataChanges)
 *
 * defaultLambda  : default lambda to execute (backend -> database -> returned value
 * onNoDataChanged: lambda to execute when 304 received from the default lambda (database -> returned value)
 */
suspend fun <T> invokeRepository(
    defaultLambda: suspend () -> Either<RepositoryBackendFailure, T>,
    onNoDataChanged: (suspend () -> Either<RepositoryBackendFailure, T>)?,
): Either<RepositoryBackendFailure, T>  where T : Any=
    try {
        defaultLambda.invoke()
    } catch (e: NetworkApiException) {
        when (e.errorCode) {
            NetworkApiErrorCode.NoDataChanges ->
                onNoDataChanged?.let {
                    // Calling invokeRepository with onNoDataChanged equals null to avoid
                    // infinite recursion
                    invokeRepository(it, null)
                } ?: run {
                    Either.Left(RepositoryBackendFailure.BackendProblems)
                }
            NetworkApiErrorCode.UnknownHost,
            NetworkApiErrorCode.Timeout,
            NetworkApiErrorCode.SSLError,
            NetworkApiErrorCode.HttpUnmanaged,
            NetworkApiErrorCode.IO -> Either.Left(RepositoryBackendFailure.ConnectionProblems)

            NetworkApiErrorCode.Http400,
            NetworkApiErrorCode.Http401,
            NetworkApiErrorCode.Http403,
            NetworkApiErrorCode.Unexpected -> Either.Left(RepositoryBackendFailure.BackendProblems)
        }
    }