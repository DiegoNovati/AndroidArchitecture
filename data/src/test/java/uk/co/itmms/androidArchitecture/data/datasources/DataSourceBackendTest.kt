package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.network.IPassApi
import uk.co.itmms.androidArchitecture.data.datasources.network.PassApiException
import uk.co.itmms.androidArchitecture.data.models.NetAuthenticateOffice
import uk.co.itmms.androidArchitecture.data.models.NetAuthenticateResponse
import uk.co.itmms.androidArchitecture.data.models.NetBookingsResponse
import uk.co.itmms.androidArchitecture.data.models.NetCustomersResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceBackendTest : BaseDataTest() {

    @MockK
    private lateinit var mockPassApi: IPassApi

    private lateinit var dataSourceBackend: DataSourceBackend

    private val username = "username"
    private val password = "password"
    private val officeBid = "officeBid"

    @Before
    fun setUp() {
        dataSourceBackend = DataSourceBackend(mockPassApi)
    }

    @Test
    fun `WHEN the authentication is successful THEN it returns the office list`() = runBlocking(
        Dispatchers.Unconfined
    ) {
        val mockAuthenticateResponse = mockk<NetAuthenticateResponse>()
        val mockOfficeList = mockk<List<NetAuthenticateOffice>>()

        coEvery { mockPassApi.authenticate(any(), any(), any()) } returns mockAuthenticateResponse
        coEvery { mockAuthenticateResponse.token } returns "<token>"
        coEvery { mockAuthenticateResponse.offices } returns mockOfficeList

        val actual = dataSourceBackend.authenticate(username, password)

        assertNotNull(actual)
        assertEquals(mockOfficeList, actual)
        assertTrue(dataSourceBackend.authorization.isNotBlank())

        coVerify(exactly = 1) {
            mockPassApi.authenticate(any(), any(), any())
        }
        confirmVerified(mockPassApi)
    }

    @Test
    fun `WHEN the authentication fails THEN it returns a PassApiException`() =
        runBlocking(Dispatchers.Unconfined) {
            coEvery { mockPassApi.authenticate(any(), any(), any()) } throws RuntimeException()

            try {
                dataSourceBackend.authenticate(username, password)
                fail("A PassApiException is expected")
            } catch (_: PassApiException) {
            }

            coVerify(exactly = 1) {
                mockPassApi.authenticate(any(), any(), any())
            }
            confirmVerified(mockPassApi)
        }

    @Test
    fun `initially the authorization field is empty`() {
        assertTrue(dataSourceBackend.authorization.isBlank())
    }

    @Test
    fun `after a successful authentication the authorization field is not empty`() =
        runBlocking(Dispatchers.Unconfined) {
            val mockAuthenticateResponse = mockk<NetAuthenticateResponse>()
            val mockOfficeList = mockk<List<NetAuthenticateOffice>>()

            coEvery { mockPassApi.authenticate(any(), any(), any()) } returns mockAuthenticateResponse
            coEvery { mockAuthenticateResponse.token } returns "<token>"
            coEvery { mockAuthenticateResponse.offices } returns mockOfficeList

            dataSourceBackend.authenticate(username, password)

            assertTrue(dataSourceBackend.authorization.isNotBlank())
        }

    @Test
    fun `after the logout the authorization field is empty`() =
        runBlocking(Dispatchers.Unconfined) {
            val mockAuthenticateResponse = mockk<NetAuthenticateResponse>()
            val mockOfficeList = mockk<List<NetAuthenticateOffice>>()

            coEvery { mockPassApi.authenticate(any(), any(), any()) } returns mockAuthenticateResponse
            coEvery { mockAuthenticateResponse.token } returns "<token>"
            coEvery { mockAuthenticateResponse.offices } returns mockOfficeList

            dataSourceBackend.authenticate(username, password)
            dataSourceBackend.logout()

            assertTrue(dataSourceBackend.authorization.isBlank())
        }

    @Test
    fun `WHEN getCustomerList is successful THEN it returns an office list`() = runBlocking {
        val mockCustomersResponse = mockk<NetCustomersResponse>()
        coEvery { mockPassApi.getCustomerList(any(), any()) } returns mockCustomersResponse

        val actual = dataSourceBackend.getCustomerList(officeBid)

        assertEquals(mockCustomersResponse, actual)

        coVerify(exactly = 1) {
            mockPassApi.getCustomerList("", officeBid)
        }
        confirmVerified(mockPassApi)
    }

    @Test
    fun `WHEN getCustomerList fails THEN it returns a PassApiException`() = runBlocking {
        coEvery { mockPassApi.getCustomerList(any(), any()) } throws RuntimeException("error !")

        try {
            dataSourceBackend.getCustomerList(officeBid)
            fail("A PassApiException is expected")
        } catch (_: PassApiException) { }
    }

    @Test
    fun `WHEN getBookingList is successful THEN it returns a booking list`() = runBlocking {
        val mockBookingsResponse = mockk<NetBookingsResponse>()
        coEvery { mockPassApi.getBookingList(any(), any()) } returns mockBookingsResponse

        val actual = dataSourceBackend.getBookingList(officeBid)

        assertEquals(mockBookingsResponse, actual)

        coVerify(exactly = 1) {
            mockPassApi.getBookingList("", officeBid)
        }
        confirmVerified(mockPassApi)
    }

    @Test
    fun `WHEN getBookingList fails THEN it returns a PassApiException`() = runBlocking {
        coEvery { mockPassApi.getBookingList(any(), any()) } throws RuntimeException("error !")

        try {
            dataSourceBackend.getBookingList(officeBid)
            fail("A PassApiException is expected")
        } catch (_: PassApiException) { }
    }
}