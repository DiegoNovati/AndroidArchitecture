package uk.co.itmms.androidArchitecture.domain.usecases.login

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
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
    companion object {
        const val fakeOfficeBid = "<fake office>"
    }

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
        val fakeBackend: Boolean = false,
        val officeBid: String = "",
        val customerList: List<Customer> = emptyList(),
        val bookingList: List<Booking> = emptyList(),
    )

    override suspend fun run(params: Params): Either<FailureLogin, Unit> =
        Runtime().right()
            .flatMap { runtime -> login(params, runtime) }
            .flatMap { runtime -> getCustomerList(runtime) }
            .flatMap { runtime -> getBookingList(runtime) }
            .flatMap { runtime -> saveDataRuntime(params, runtime) }
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

    private suspend fun getCustomerList(runtime: Runtime): Either<FailureLogin, Runtime> =
        if (runtime.fakeBackend) {
            runtime.copy(
                customerList = fakeCustomerList,
            ).right()
        } else {
            repositoryCustomers.getCustomerList(officeBid = runtime.officeBid)
                .flatMap { customerList -> runtime.copy(customerList = customerList).right() }
                .mapLeft { failure -> failure.toLoginFailure() }
        }

    private suspend fun getBookingList(runtime: Runtime): Either<FailureLogin, Runtime> =
        if (runtime.fakeBackend) {
            runtime.copy(
                bookingList = fakeBookingList,
            ).right()
        } else {
            repositoryBookings.getBookingList(officeBid = runtime.officeBid)
                .flatMap { bookingList -> runtime.copy(bookingList = bookingList).right() }
                .mapLeft { failure -> failure.toLoginFailure() }
        }

    private suspend fun saveDataRuntime(params: Params, runtime: Runtime): Either<FailureLogin, Runtime> {
        repositoryRuntime.setOfficeBid(runtime.officeBid)
        repositoryRuntime.setCustomerList(runtime.customerList)
        repositoryRuntime.setBookingList(runtime.bookingList)
        repositoryRuntime.setAuthenticated(true)
        repositoryRuntime.setFakeAuthenticationExpire(params.fakeAuthenticationExpire)
        return runtime.right()
    }

    private val fakeCustomerList: List<Customer> by lazy {
        listOf(
            Customer(customerBid = "1", name = "Hadassah Watkins", address = "8482 Sed St."),
            Customer(customerBid = "2", name = "Jada Talley", address = "8010 Elit. Rd."),
            Customer(customerBid = "3", name = "Penelope Emerson", address = "8599 Nunc Road"),
            Customer(customerBid = "4", name = "Tiger Chapman", address = "4362 Sem, Roa"),
            Customer(customerBid = "5", name = "Olivia Carrillo", address = ""),
            Customer(customerBid = "6", name = "Hop Parrish", address = "5551 Duis Avenue"),
            Customer(customerBid = "7", name = "Kyra Maldonado", address = "394 Sed St."),
            Customer(customerBid = "8", name = "Dolan Flynn", address = "6897 Felis. Rd."),
            Customer(customerBid = "9", name = "Hashim Chambers", address = "9801 Dolor. St."),
            Customer(customerBid = "10", name = "Kuame Decker", address = "3563 Lectus Ave"),
            Customer(customerBid = "11", name = "Cara Hurst", address = "7369 Vel St."),
            Customer(customerBid = "12", name = "Kevyn Pope", address = "3709 Neque Avenue"),
        )
    }

    private val fakeBookingList: List<Booking> by lazy {
        listOf(
            Booking(
                bookingBid = 1,
                customerBid = "1",
                status = BookingStatus.Scheduled,
                start = "01:00:44".toDate(),
                end = "08:30:09".toDate()
            ),
            Booking(
                bookingBid = 2,
                customerBid = "2",
                status = BookingStatus.Scheduled,
                start = "08:28:38".toDate(),
                end = "13:17:55".toDate()
            ),
            Booking(
                bookingBid = 3,
                customerBid = "4",
                status = BookingStatus.Started,
                start = "13:08:45".toDate(),
                end = "15:01:47".toDate()
            ),
            Booking(
                bookingBid = 4,
                customerBid = "5",
                status = BookingStatus.Completed,
                start = "00:09:02".toDate(),
                end = "22:09:20".toDate()
            ),
            Booking(
                bookingBid = 5,
                customerBid = "7",
                status = BookingStatus.Scheduled,
                start = "04:44:54".toDate(),
                end = "10:27:46".toDate()
            ),
            Booking(
                bookingBid = 6,
                customerBid = "9",
                status = BookingStatus.Completed,
                start = "08:42:41".toDate(),
                end = "20:55:35".toDate()
            ),
            Booking(
                bookingBid = 7,
                customerBid = "10",
                status = BookingStatus.Completed,
                start = "13:10:03".toDate(),
                end = "20:41:17".toDate()
            ),
            Booking(
                bookingBid = 8,
                customerBid = "12",
                status = BookingStatus.Scheduled,
                start = "03:36:10".toDate(),
                end = "18:53:42".toDate()
            ),
        )
    }

    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    private fun String.toDate(): Date =
        simpleDateFormat.parse(this)
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

internal fun RepositoryBackendFailure.toLoginFailure(): FailureLogin =
    when (this) {
        RepositoryBackendFailure.BackendProblems -> FailureLogin.BackendProblems
        RepositoryBackendFailure.ConnectionProblems -> FailureLogin.ConnectionProblems
    }