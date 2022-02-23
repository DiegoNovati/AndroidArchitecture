package com.elt.passsystem.domain.usecases.authentication

import arrow.core.Either
import arrow.core.flatMap
import com.elt.passsystem.domain.entities.AuthenticationLoginFailure
import com.elt.passsystem.domain.entities.LoginResult
import com.elt.passsystem.domain.repositories.*
import com.elt.passsystem.domain.usecases.UseCaseSingle

class UseCaseAuthenticationLogin(
    repositoryLogger: IRepositoryLogger,
    repositoryAnalytics: IRepositoryAnalytics,
    private val repositoryAuthentication: IRepositoryAuthentication,
    private val repositoryCustomers: IRepositoryCustomers,
    private val repositoryBookings: IRepositoryBookings,
) : UseCaseSingle<UseCaseAuthenticationLogin.Params, LoginResult, AuthenticationLoginFailure>(
    repositoryLogger, repositoryAnalytics
) {

    data class Params(
        val username: String,
        val password: String,
    ) {
        /**
         * We don't want to show the password when the params are logged
         */
        override fun toString(): String =
            "Params(username=$username, password=**********)"
    }

    override suspend fun run(params: Params): Either<AuthenticationLoginFailure, LoginResult> =
        login(params)
            .flatMap { getCustomerList(it) }
            .flatMap { getBookingList(it) }

    internal suspend fun login(params: Params): Either<AuthenticationLoginFailure, LoginResult> {
        val retValue = repositoryAuthentication.login(
            userName = params.username,
            password = params.password,
        )
        return retValue.fold({ failure ->
            Either.Left(failure.toLoginFailure())
        }) { login ->
            Either.Right(LoginResult(login.officeBid))
        }
    }

    internal suspend fun getCustomerList(loginResult: LoginResult): Either<AuthenticationLoginFailure, LoginResult> {
        val retValue = repositoryCustomers.getCustomerList(loginResult.officeBid)
        return retValue.fold({ failure ->
            Either.Left(failure.toLoginFailure())
        }) { customerList ->
            Either.Right(loginResult.copy(customerList = customerList))
        }
    }

    internal suspend fun getBookingList(loginResult: LoginResult): Either<AuthenticationLoginFailure, LoginResult> {
        val retValue = repositoryBookings.getBookingList(loginResult.officeBid)
        return retValue.fold({ failure ->
            Either.Left(failure.toLoginFailure())
        }) { bookingList ->
            Either.Right(loginResult.copy(bookingList = bookingList))
        }
    }
}

internal fun IRepositoryAuthentication.RepositoryAuthenticationFailure.toLoginFailure(): AuthenticationLoginFailure =
    when (this) {
        IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems ->
            AuthenticationLoginFailure.BackendProblems
        IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems ->
            AuthenticationLoginFailure.ConnectionProblems
        IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError ->
            AuthenticationLoginFailure.LoginError
    }

internal fun RepositoryBackendFailure.toLoginFailure(): AuthenticationLoginFailure =
    when (this) {
        RepositoryBackendFailure.BackendProblems -> AuthenticationLoginFailure.BackendProblems
        RepositoryBackendFailure.ConnectionProblems -> AuthenticationLoginFailure.ConnectionProblems
    }