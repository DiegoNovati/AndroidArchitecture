package uk.co.itmms.androidArchitecture.domain.usecases.login

import arrow.core.left
import arrow.core.right
import uk.co.itmms.androidArchitecture.domain.BaseDomainTest
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.failures.FailureLogin
import uk.co.itmms.androidArchitecture.domain.repositories.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class UseCaseLoginLoginTest : BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryAuthentication: IRepositoryAuthentication

    @MockK
    private lateinit var mockRepositoryRuntime: IRepositoryRuntime

    private lateinit var useCaseLoginLogin: UseCaseLoginLogin

    private val params = UseCaseLoginLogin.Params(
        username = "the username",
        password = "the password",
        fakeBackend = false,
        fakeAuthenticationExpire = false,
    )

    private val officeBid = "office_bid"

    private val result = IRepositoryAuthentication.Result(officeBid)
    private val customerList = listOf(
        Customer(customerBid = "1", name = "Name 1", address = "Address 1"),
        Customer(customerBid = "2", name = "Name 2", address = "Address 2"),
    )
    private val bookingList = listOf(
        Booking(bookingBid= 1, customerBid = "1", status = BookingStatus.Completed, start = Date(), end = Date()),
        Booking(bookingBid= 2, customerBid = "2", status = BookingStatus.Scheduled, start = Date(), end = Date()),
    )

    @Before
    fun setUp() {
        useCaseLoginLogin = UseCaseLoginLogin(
            mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics, mockRepositoryAuthentication,
            mockRepositoryCustomers, mockRepositoryBookings, mockRepositoryRuntime,
        )
    }

    @Test
    fun `WHEN login is successful THEN returns a right value`() = runBlocking {
        coEvery { mockRepositoryAuthentication.login(any(), any()) } returns result.right()
        coEvery { mockRepositoryCustomers.getCustomerList(any()) } returns customerList.right()
        coEvery { mockRepositoryBookings.getBookingList(any()) } returns bookingList.right()

        val actual = useCaseLoginLogin.run(params)

        actual.isRight()

        coVerify(exactly = 1) {
            mockRepositoryAuthentication.login(params.username, params.password)
            mockRepositoryCustomers.getCustomerList(officeBid)
            mockRepositoryBookings.getBookingList(officeBid)
            mockRepositoryRuntime.setOfficeBid(officeBid)
            mockRepositoryRuntime.setCustomerList(customerList)
            mockRepositoryRuntime.setBookingList(bookingList)
            mockRepositoryRuntime.setAuthenticated(true)
            mockRepositoryRuntime.setFakeAuthenticationExpire(params.fakeAuthenticationExpire)
        }
        confirmVerified(mockRepositoryAuthentication, mockRepositoryCustomers,
            mockRepositoryBookings, mockRepositoryRuntime,
        )
    }

    @Test
    fun `WHEN login fails THEN returns a left value`() = runBlocking {
        coEvery { mockRepositoryAuthentication.login(any(), any()) } returns IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError.left()

        val actual = useCaseLoginLogin.run(params)

        actual.fold({
            assertEquals(FailureLogin.LoginError, it)
        }){
            fail("Unexpected success")
        }

        coVerify(exactly = 1) {
            mockRepositoryAuthentication.login(params.username, params.password)
        }
        confirmVerified(mockRepositoryAuthentication, mockRepositoryCustomers,
            mockRepositoryBookings, mockRepositoryRuntime,
        )
    }

    @Test
    fun `WHEN getCustomerList fails THEN returns a left value`() = runBlocking {
        coEvery { mockRepositoryAuthentication.login(any(), any()) } returns result.right()
        coEvery { mockRepositoryCustomers.getCustomerList(any()) } returns RepositoryBackendFailure.BackendProblems.left()

        val actual = useCaseLoginLogin.run(params)

        actual.fold({
            assertEquals(FailureLogin.BackendProblems, it)
        }){
            fail("Unexpected success")
        }

        coVerify(exactly = 1) {
            mockRepositoryAuthentication.login(params.username, params.password)
            mockRepositoryCustomers.getCustomerList(any())

        }
        confirmVerified(mockRepositoryAuthentication, mockRepositoryCustomers,
            mockRepositoryBookings, mockRepositoryRuntime,
        )
    }

    @Test
    fun `WHEN getBookingList fails THEN returns a left value`() = runBlocking {
        coEvery { mockRepositoryAuthentication.login(any(), any()) } returns result.right()
        coEvery { mockRepositoryCustomers.getCustomerList(any()) } returns customerList.right()
        coEvery { mockRepositoryBookings.getBookingList(any()) } returns RepositoryBackendFailure.ConnectionProblems.left()

        val actual = useCaseLoginLogin.run(params)

        actual.fold({
            assertEquals(FailureLogin.ConnectionProblems, it)
        }){
            fail("Unexpected success")
        }

        coVerify(exactly = 1) {
            mockRepositoryAuthentication.login(params.username, params.password)
            mockRepositoryCustomers.getCustomerList(officeBid)
            mockRepositoryBookings.getBookingList(officeBid)
        }
        confirmVerified(mockRepositoryAuthentication, mockRepositoryCustomers,
            mockRepositoryBookings, mockRepositoryRuntime,
        )
    }

    @Test
    fun `testing AuthenticationFailure toLoginFailure`() {
        var actual = IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems.toLoginFailure()
        assertEquals(FailureLogin.BackendProblems, actual)

        actual = IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems.toLoginFailure()
        assertEquals(FailureLogin.ConnectionProblems, actual)

        actual = IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError.toLoginFailure()
        assertEquals(FailureLogin.LoginError, actual)
    }

    @Test
    fun `testing BackendFailure toLoginFailure`() {
        var actual = RepositoryBackendFailure.BackendProblems.toLoginFailure()
        assertEquals(FailureLogin.BackendProblems, actual)

        actual = RepositoryBackendFailure.ConnectionProblems.toLoginFailure()
        assertEquals(FailureLogin.ConnectionProblems, actual)
    }
}