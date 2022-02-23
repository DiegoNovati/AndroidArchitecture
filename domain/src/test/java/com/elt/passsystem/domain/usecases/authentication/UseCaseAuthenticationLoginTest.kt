package com.elt.passsystem.domain.usecases.authentication

import arrow.core.Either
import com.elt.passsystem.domain.BaseDomainTest
import com.elt.passsystem.domain.entities.*
import com.elt.passsystem.domain.repositories.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UseCaseAuthenticationLoginTest : BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryLogger: IRepositoryLogger

    @MockK
    private lateinit var mockRepositoryAnalytics: IRepositoryAnalytics

    @MockK
    private lateinit var mockRepositoryAuthentication: IRepositoryAuthentication

    @MockK
    private lateinit var mockRepositoryCustomers: IRepositoryCustomers

    @MockK
    private lateinit var mockRepositoryBookings: IRepositoryBookings

    private lateinit var useCaseAuthenticationLogin: UseCaseAuthenticationLogin

    private val params = UseCaseAuthenticationLogin.Params(
        username = "the username",
        password = "the password",
    )

    private val officeBid = "office_bid"

    @Before
    fun setUp() {
        useCaseAuthenticationLogin = UseCaseAuthenticationLogin(
            mockRepositoryLogger, mockRepositoryAnalytics, mockRepositoryAuthentication,
            mockRepositoryCustomers, mockRepositoryBookings,
        )
    }

    @Test
    fun `testing run`() = runBlocking {
        val spy = spyk(useCaseAuthenticationLogin)
        val mockLoginResultLogin = mockk<LoginResult>()
        val mockLoginResultCustomers = mockk<LoginResult>()
        val mockLoginResultBookings = mockk<LoginResult>()

        coEvery { spy.login(params) } returns Either.Right(mockLoginResultLogin)
        every { mockLoginResultLogin.officeBid } returns officeBid
        coEvery { spy.getCustomerList(any()) } returns Either.Right(mockLoginResultCustomers)
        every { mockLoginResultCustomers.customerList } returns mockk()
        coEvery { spy.getBookingList(any()) } returns Either.Right(mockLoginResultBookings)
        every { mockLoginResultBookings.bookingList } returns mockk()

        spy.run(params)

        coVerify(exactly = 1) {
            spy.run(params)
            spy.login(params)
            spy.getCustomerList(any())
            spy.getBookingList(any())
        }
        confirmVerified(spy)
    }

    @Test
    fun `WHEN login is successful THEN returns a right value`() = runBlocking {
        val mockLogin = mockk<Login>()
        coEvery { mockRepositoryAuthentication.login(any(), any()) } returns Either.Right(mockLogin)
        every { mockLogin.officeBid } returns officeBid

        val actual = useCaseAuthenticationLogin.login(params)

        assertTrue(actual.isRight())
        actual.fold({}){
            assertEquals(officeBid, it.officeBid)
        }

        coVerify(exactly = 1) {
            mockRepositoryAuthentication.login(params.username, params.password)
        }
        confirmVerified(mockRepositoryAuthentication)
    }

    @Test
    fun `WHEN login fails THEN returns a left value`() = runBlocking {
        coEvery { mockRepositoryAuthentication.login(any(), any()) } returns Either.Left(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError)

        val actual = useCaseAuthenticationLogin.login(params)

        assertTrue(actual.isLeft())
        actual.fold({
            assertEquals(AuthenticationLoginFailure.LoginError, it)
        }){}

        coVerify(exactly = 1) {
            mockRepositoryAuthentication.login(params.username, params.password)
        }
        confirmVerified(mockRepositoryAuthentication)
    }

    @Test
    fun `WHEN getCustomerList is successful THEN returns a right value`() = runBlocking {
        val mockLoginResult = mockk<LoginResult>()
        val mockCustomerList = mockk<List<Customer>>()
        val mockLoginResultResponse = mockk<LoginResult>()
        coEvery { mockRepositoryCustomers.getCustomerList(any()) } returns Either.Right(mockCustomerList)
        every { mockLoginResult.officeBid } returns officeBid
        every { mockLoginResult.copy(customerList = mockCustomerList) } returns mockLoginResultResponse

        val actual = useCaseAuthenticationLogin.getCustomerList(mockLoginResult)

        assertTrue(actual.isRight())
        actual.fold({}){
            assertEquals(mockLoginResultResponse, it)
        }

        coVerify(exactly = 1) {
            mockRepositoryCustomers.getCustomerList(officeBid)
        }
        confirmVerified(mockRepositoryCustomers)
    }

    @Test
    fun `WHEN getCustomerList fails THEN returns a left value`() = runBlocking {
        val mockLoginResult = mockk<LoginResult>()
        coEvery { mockRepositoryCustomers.getCustomerList(any()) } returns Either.Left(RepositoryBackendFailure.ConnectionProblems)
        every { mockLoginResult.officeBid } returns officeBid

        val actual = useCaseAuthenticationLogin.getCustomerList(mockLoginResult)

        assertTrue(actual.isLeft())
        actual.fold({
            assertEquals(AuthenticationLoginFailure.ConnectionProblems, it)
        }){}

        coVerify(exactly = 1) {
            mockRepositoryCustomers.getCustomerList(any())
        }
        confirmVerified(mockRepositoryCustomers)
    }

    @Test
    fun `WHEN getBookingList is successful THEN returns a right value`() = runBlocking {
        val mockLoginResult = mockk<LoginResult>()
        val mockBookingList = mockk<List<Booking>>()
        val mockLoginResultResponse = mockk<LoginResult>()
        coEvery { mockRepositoryBookings.getBookingList(any()) } returns Either.Right(mockBookingList)
        every { mockLoginResult.officeBid } returns officeBid
        every { mockLoginResult.copy(bookingList = mockBookingList) } returns mockLoginResultResponse

        val actual = useCaseAuthenticationLogin.getBookingList(mockLoginResult)

        assertTrue(actual.isRight())
        actual.fold({}){
            assertEquals(mockLoginResultResponse, it)
        }

        coVerify(exactly = 1) {
            mockRepositoryBookings.getBookingList(officeBid)
        }
        confirmVerified(mockRepositoryBookings)
    }

    @Test
    fun `WHEN getBookingList fails THEN returns a left value`() = runBlocking {
        val mockLoginResult = mockk<LoginResult>()
        coEvery { mockRepositoryBookings.getBookingList(any()) } returns Either.Left(RepositoryBackendFailure.ConnectionProblems)
        every { mockLoginResult.officeBid } returns officeBid

        val actual = useCaseAuthenticationLogin.getBookingList(mockLoginResult)

        assertTrue(actual.isLeft())
        actual.fold({
            assertEquals(AuthenticationLoginFailure.ConnectionProblems, it)
        }){}

        coVerify(exactly = 1) {
            mockRepositoryBookings.getBookingList(any())
        }
        confirmVerified(mockRepositoryBookings)
    }

    @Test
    fun `testing AuthenticationFailure toLoginFailure`() {
        var actual = IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems.toLoginFailure()

        assertEquals(AuthenticationLoginFailure.BackendProblems, actual)

        actual = IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems.toLoginFailure()

        assertEquals(AuthenticationLoginFailure.ConnectionProblems, actual)

        actual = IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError.toLoginFailure()

        assertEquals(AuthenticationLoginFailure.LoginError, actual)
    }

    @Test
    fun `testing BackendFailure toLoginFailure`() {
        var actual = RepositoryBackendFailure.BackendProblems.toLoginFailure()

        assertEquals(AuthenticationLoginFailure.BackendProblems, actual)

        actual = RepositoryBackendFailure.ConnectionProblems.toLoginFailure()

        assertEquals(AuthenticationLoginFailure.ConnectionProblems, actual)
    }
}