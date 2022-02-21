package com.elt.passsystem.domain.usecases.authentication

import arrow.core.Either
import com.elt.passsystem.domain.entities.Booking
import com.elt.passsystem.domain.entities.BookingStatus
import com.elt.passsystem.domain.entities.Customer
import com.elt.passsystem.domain.entities.LoginResult
import com.elt.passsystem.domain.repositories.IRepositoryAnalytics
import com.elt.passsystem.domain.repositories.IRepositoryLogger
import com.elt.passsystem.domain.usecases.Failure
import com.elt.passsystem.domain.usecases.UseCaseSingle
import kotlinx.coroutines.delay

class UseCaseAuthenticationLogin(
    repositoryLogger: IRepositoryLogger,
    repositoryAnalytics: IRepositoryAnalytics,
) : UseCaseSingle<UseCaseAuthenticationLogin.Params, LoginResult>(
    repositoryLogger, repositoryAnalytics
) {

    data class Params(
        val username: String,
        val password: String,
    )

    sealed class LoginFailure : Failure.FeatureFailure() {
        object ConnectionProblems : LoginFailure()
        object BackendProblems : LoginFailure()
        object LoginError: LoginFailure()
    }

    override suspend fun run(params: Params): Either<Failure, LoginResult> {
        delay(500)
        return when ((0..3).random()) {
            0 -> createResponseSuccess()
            1 -> createResponseLoginError()
            2 -> createResponseConnectionProblems()
            else -> createResponseBackendProblems()
        }
    }

    private fun createResponseSuccess(): Either<Failure, LoginResult> =
        Either.Right(
            LoginResult(
                officeBid = "office BID",
                customerList = listOf(
                    Customer("customer1", "Mr John Smith", "45 Oxford Circus, London")
                ),
                bookingList = listOf(
                    Booking("booking1", "customer1", BookingStatus.Started),
                    Booking("booking2", "customer1", BookingStatus.Scheduled),
                    Booking("booking3", "customer1", BookingStatus.Scheduled),
                ),
            )
        )

    private fun createResponseLoginError(): Either<Failure, LoginResult> =
        Either.Left(LoginFailure.LoginError)

    private fun createResponseConnectionProblems(): Either<Failure, LoginResult> =
        Either.Left(LoginFailure.ConnectionProblems)

    private fun createResponseBackendProblems(): Either<Failure, LoginResult> =
        Either.Left(LoginFailure.BackendProblems)
}