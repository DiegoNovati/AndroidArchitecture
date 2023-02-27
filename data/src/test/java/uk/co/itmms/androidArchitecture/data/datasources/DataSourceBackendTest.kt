package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.network.IBackend
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
import uk.co.itmms.androidArchitecture.data.models.NetAuthLoginResponse
import uk.co.itmms.androidArchitecture.data.models.NetProductsResponse
import uk.co.itmms.androidArchitecture.data.models.NetTodosResponse

class DataSourceBackendTest : BaseDataTest() {

    @MockK
    private lateinit var mockBackend: IBackend

    private lateinit var dataSourceBackend: DataSourceBackend

    private val username = "username"
    private val password = "password"

    @Before
    fun setUp() {
        dataSourceBackend = DataSourceBackend(mockBackend)
    }

    @Test
    fun `WHEN the login is successful THEN it returns a valid response`() = runBlocking {
        val mockNetAuthLoginResponse = mockk<NetAuthLoginResponse>()

        coEvery { mockBackend.login(any()) } returns mockNetAuthLoginResponse

        val actual = dataSourceBackend.login(username, password)

        assertEquals(mockNetAuthLoginResponse, actual)

        coVerify(exactly = 1) {
            mockBackend.login(any())
        }
        confirmVerified(mockBackend)
    }

    @Test
    fun `WHEN the login fails THEN it returns a BackendException`() = runBlocking {
        coEvery { mockBackend.login(any()) } throws RuntimeException()

        try {
            dataSourceBackend.login(username, password)
            fail("A BackendException is expected")
        } catch (_: BackendException) {}

        coVerify(exactly = 1) {
            mockBackend.login(any())
        }
        confirmVerified(mockBackend)
    }

    @Test
    fun `WHEN getProducts succeeds THEN it returns a list of products`() = runBlocking {
        val mockProductsResponse = mockk<NetProductsResponse>()
        coEvery { mockBackend.getProducts() } returns mockProductsResponse

        val actual = dataSourceBackend.getProducts()

        assertEquals(mockProductsResponse, actual)

        coVerify(exactly = 1) {
            mockBackend.getProducts()
        }
        confirmVerified(mockBackend)
    }

    @Test
    fun `WHEN getProducts fails THEN it returns a BackendException`() = runBlocking {
        coEvery { mockBackend.getProducts() } throws RuntimeException()

        try {
            dataSourceBackend.getProducts()
            fail("A BackendException is expected")
        } catch (_: BackendException) {}

        coVerify(exactly = 1) {
            mockBackend.getProducts()
        }
        confirmVerified(mockBackend)
    }

    @Test
    fun `WHEN getTodos succeeds THEN it returns a list of products`() = runBlocking {
        val mockTodosResponse = mockk<NetTodosResponse>()
        coEvery { mockBackend.getTodos() } returns mockTodosResponse

        val actual = dataSourceBackend.getTodos()

        assertEquals(mockTodosResponse, actual)

        coVerify(exactly = 1) {
            mockBackend.getTodos()
        }
        confirmVerified(mockBackend)
    }

    @Test
    fun `WHEN getTodos fails THEN it returns a BackendException`() = runBlocking {
        coEvery { mockBackend.getTodos() } throws RuntimeException()

        try {
            dataSourceBackend.getTodos()
            fail("A BackendException is expected")
        } catch (_: BackendException) {
        }

        coVerify(exactly = 1) {
            mockBackend.getTodos()
        }
        confirmVerified(mockBackend)
    }
}