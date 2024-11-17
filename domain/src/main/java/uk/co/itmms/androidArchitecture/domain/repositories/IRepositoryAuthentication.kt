package uk.co.itmms.androidArchitecture.domain.repositories

import arrow.core.Either

interface IRepositoryAuthentication {

    sealed class RepositoryAuthenticationFailure {
        data object ConnectionProblems : RepositoryAuthenticationFailure()
        data object BackendProblems : RepositoryAuthenticationFailure()
        data object LoginError: RepositoryAuthenticationFailure()
    }

    data class ResultLogin(
        val officeBid: String,
    )

    suspend fun login(userName: String, password: String): Either<RepositoryAuthenticationFailure, ResultLogin>
    suspend fun logout()
}