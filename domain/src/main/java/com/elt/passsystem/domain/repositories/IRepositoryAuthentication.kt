package com.elt.passsystem.domain.repositories

import arrow.core.Either
import com.elt.passsystem.domain.entities.Login

interface IRepositoryAuthentication {

    sealed class RepositoryAuthenticationFailure {
        object ConnectionProblems : RepositoryAuthenticationFailure()
        object BackendProblems : RepositoryAuthenticationFailure()
        object LoginError: RepositoryAuthenticationFailure()
    }

    suspend fun login(userName: String, password: String): Either<RepositoryAuthenticationFailure, Login>
    suspend fun logout()
}