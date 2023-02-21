package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.data.datasources.network.PassApiErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.PassApiException
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
    } catch (e: PassApiException) {
        when (e.errorCode) {
            PassApiErrorCode.NoDataChanges ->
                onNoDataChanged?.let {
                    // Calling invokeRepository with onNoDataChanged equals null to avoid
                    // infinite recursion
                    invokeRepository(it, null)
                } ?: run {
                    Either.Left(RepositoryBackendFailure.BackendProblems)
                }
            PassApiErrorCode.UnknownHost,
            PassApiErrorCode.Timeout,
            PassApiErrorCode.SSLError,
            PassApiErrorCode.HttpUnmanaged,
            PassApiErrorCode.IO -> Either.Left(RepositoryBackendFailure.ConnectionProblems)

            PassApiErrorCode.Http400,
            PassApiErrorCode.Http401,
            PassApiErrorCode.Http403,
            PassApiErrorCode.Unexpected -> Either.Left(RepositoryBackendFailure.BackendProblems)
        }
    }