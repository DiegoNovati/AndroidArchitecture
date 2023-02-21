package uk.co.itmms.androidArchitecture.domain.repositories

import arrow.core.Either

interface IRepositoryAuthentication {

    sealed class RepositoryAuthenticationFailure {
        object ConnectionProblems : RepositoryAuthenticationFailure()
        object BackendProblems : RepositoryAuthenticationFailure()
        object LoginError: RepositoryAuthenticationFailure()
    }

    data class ResultLogin(
        val officeBid: String,
    )

    suspend fun login(userName: String, password: String): Either<RepositoryAuthenticationFailure, ResultLogin>
    suspend fun logout()
}