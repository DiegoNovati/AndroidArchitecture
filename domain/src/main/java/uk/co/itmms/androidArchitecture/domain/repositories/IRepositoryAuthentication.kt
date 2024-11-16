package uk.co.itmms.androidArchitecture.domain.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.entities.User
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackendAuthentication

interface IRepositoryAuthentication {

    data class Result(
        val user: User,
        val token: String,
    )

    suspend fun login(userName: String, password: String): Either<FailureRepositoryBackendAuthentication, Result>
}