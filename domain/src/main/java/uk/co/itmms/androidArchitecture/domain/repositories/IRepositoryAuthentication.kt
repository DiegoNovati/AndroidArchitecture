package uk.co.itmms.androidArchitecture.domain.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.entities.User

interface IRepositoryAuthentication {

    sealed class RepositoryAuthenticationFailure {
        object ConnectionProblems : RepositoryAuthenticationFailure()
        object BackendProblems : RepositoryAuthenticationFailure()
        object LoginError: RepositoryAuthenticationFailure()
    }

    data class ResultLogin(
        val user: User,
        val token: String,
    )

    suspend fun login(userName: String, password: String): Either<RepositoryAuthenticationFailure, ResultLogin>
}