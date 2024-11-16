package uk.co.itmms.androidArchitecture.domain.usecases.login

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import sun.security.jgss.GSSUtil.login
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.failures.FailureLogin
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackendAuthentication
import uk.co.itmms.androidArchitecture.domain.repositories.*
import uk.co.itmms.androidArchitecture.domain.usecases.UseCaseBase
import java.text.SimpleDateFormat
import java.util.*

class UseCaseLoginLogin(
    repositoryLogger: IRepositoryDevelopmentLogger,
    repositoryAnalytics: IRepositoryDevelopmentAnalytics,
    private val repositoryAuthentication: IRepositoryAuthentication,
    private val repositoryRuntime: IRepositoryRuntime,
) : UseCaseBase<UseCaseLoginLogin.Params, Unit, FailureLogin>(
    repositoryLogger, repositoryAnalytics
) {
    data class Params(
        val username: String,
        val password: String,
        val fakeBackend: Boolean,
        val fakeAuthenticationExpire: Boolean,
    ) {
        /**
         * We don't want to show the password when the params are logged
         */
        override fun toString(): String =
            "Params(username=$username, password=**********, fakeBackend=$fakeBackend)"
    }

    data class Runtime(
        private val loginResult: IRepositoryAuthentication.Result? = null,
    )

    override suspend fun run(params: Params): Either<FailureLogin, Unit> =
        Runtime().right()
            .flatMap { runtime -> login(params, runtime) {}
            .flatMap { Unit.right() }

    private suspend fun login(params: Params, runtime: Runtime): Either<FailureLogin, Runtime> =
        if (params.fakeBackend) {
            runtime.copy(
                fakeBackend = true,
                officeBid = fakeOfficeBid,
            ).right()
        } else {
            repositoryAuthentication.login(
                userName = params.username,
                password = params.password,
            )
                .flatMap { resultLogin -> runtime.copy(officeBid = "officeBid").right() }
                .mapLeft { failure -> failure.toLoginFailure() }
        }
}

internal fun FailureRepositoryBackendAuthentication.toLoginFailure(): FailureLogin =
    when (this) {
        FailureRepositoryBackendAuthentication.BackendError ->
            FailureLogin.BackendProblems
        FailureRepositoryBackendAuthentication.ConnectionError ->
            FailureLogin.ConnectionProblems
        FailureRepositoryBackendAuthentication.LoginError ->
            FailureLogin.LoginError
    }