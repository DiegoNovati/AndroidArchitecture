package com.elt.passsystem.domain.usecases.authentication

import arrow.core.Either
import arrow.core.flatMap
import com.elt.passsystem.domain.entities.LoginResult
import com.elt.passsystem.domain.repositories.*
import com.elt.passsystem.domain.usecases.Failure
import com.elt.passsystem.domain.usecases.UseCaseSingle

class UseCaseAuthenticationLogin(
    repositoryLogger: IRepositoryLogger,
    repositoryAnalytics: IRepositoryAnalytics,
    private val repositoryAuthentication: IRepositoryAuthentication,
    private val repositoryCustomers: IRepositoryCustomers,
    private val repositoryBookings: IRepositoryBookings,
) : UseCaseSingle<UseCaseAuthenticationLogin.Params, LoginResult>(
    repositoryLogger, repositoryAnalytics
) {

    data class Params(
        val username: String,
        val password: String,
    ) {
        override fun toString(): String =
            "Params(username=$username, password=**********)"
    }

    sealed class LoginFailure : Failure.FeatureFailure() {
        object ConnectionProblems : LoginFailure()
        object BackendProblems : LoginFailure()
        object LoginError : LoginFailure()
    }

    override suspend fun run(params: Params): Either<Failure, LoginResult> =
        login(params)
            .flatMap { getCustomerList(it) }
            .flatMap { getBookingList(it) }

    internal suspend fun login(params: Params): Either<LoginFailure, LoginResult> {
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

    internal suspend fun getCustomerList(loginResult: LoginResult): Either<LoginFailure, LoginResult> {
        val retValue = repositoryCustomers.getCustomerList(loginResult.officeBid)
        return retValue.fold({ failure ->
            Either.Left(failure.toLoginFailure())
        }) { customerList ->
            Either.Right(loginResult.copy(customerList = customerList))
        }
    }

    internal suspend fun getBookingList(loginResult: LoginResult): Either<LoginFailure, LoginResult> {
        val retValue = repositoryBookings.getBookingList(loginResult.officeBid)
        return retValue.fold({ failure ->
            Either.Left(failure.toLoginFailure())
        }) { bookingList ->
            Either.Right(loginResult.copy(bookingList = bookingList))
        }
    }
}

internal fun IRepositoryAuthentication.RepositoryAuthenticationFailure.toLoginFailure(): UseCaseAuthenticationLogin.LoginFailure =
    when (this) {
        IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems ->
            UseCaseAuthenticationLogin.LoginFailure.BackendProblems
        IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems ->
            UseCaseAuthenticationLogin.LoginFailure.ConnectionProblems
        IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError ->
            UseCaseAuthenticationLogin.LoginFailure.LoginError
    }

internal fun RepositoryBackendFailure.toLoginFailure(): UseCaseAuthenticationLogin.LoginFailure =
    when (this) {
        RepositoryBackendFailure.BackendProblems -> UseCaseAuthenticationLogin.LoginFailure.BackendProblems
        RepositoryBackendFailure.ConnectionProblems -> UseCaseAuthenticationLogin.LoginFailure.ConnectionProblems
    }