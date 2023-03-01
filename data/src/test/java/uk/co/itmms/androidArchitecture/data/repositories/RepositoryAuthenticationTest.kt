package uk.co.itmms.androidArchitecture.data.repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
import uk.co.itmms.androidArchitecture.data.models.NetAuthLoginResponse
import uk.co.itmms.androidArchitecture.domain.failures.FailureRepositoryBackendAuthentication

class RepositoryAuthenticationTest : BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceBackend: IDataSourceBackend

    private lateinit var repositoryAuthentication: RepositoryAuthentication

    private val username = "username"
    private val password = "password"

    @Before
    fun setUp() {
        repositoryAuthentication = RepositoryAuthentication(
            mockDataSourceBackend
        )
    }

    @Test
    fun `WHEN login is successful THEN returns a right value`() = runBlocking {
        val netAuthLoginResponse = createNetAuthLoginResponse()

        coEvery { mockDataSourceBackend.login(any(), any()) } returns netAuthLoginResponse

        val actual = repositoryAuthentication.login(username, password)

        actual.fold({
            fail("Unexpected failure")
        }){ result ->
            assertEquals(netAuthLoginResponse.toResultLogin(), result)
        }

        coVerify(exactly = 1) {
            mockDataSourceBackend.login(username, password)
        }
        confirmVerified(mockDataSourceBackend)
    }

    @Test
    fun `WHEN login fails THEN returns a left value`() = runBlocking {
        coEvery { mockDataSourceBackend.login(any(), any()) } throws BackendException(BackendErrorCode.Http400)

        val actual = repositoryAuthentication.login(username, password)

        actual.fold({ failure ->
            assertEquals(FailureRepositoryBackendAuthentication.LoginError, failure)
        }){
            fail("Unexpected success")
        }

        coVerify(exactly = 1) {
            mockDataSourceBackend.login(username, password)
        }
        confirmVerified(mockDataSourceBackend)
    }

    @Test
    fun `WHEN login throws an exception THEN returns a left value`() = runBlocking {
        coEvery { mockDataSourceBackend.login(any(), any()) } throws BackendException(BackendErrorCode.SSLError)

        val actual = repositoryAuthentication.login(username, password)

        actual.fold({ failure ->
            assertEquals(FailureRepositoryBackendAuthentication.ConnectionError, failure)
        }){
            fail("Unexpected success")
        }

        coVerify(exactly = 1) {
            mockDataSourceBackend.login(username, password)
        }
        confirmVerified(mockDataSourceBackend)
    }

    private fun createNetAuthLoginResponse(): NetAuthLoginResponse =
        NetAuthLoginResponse(
            id = 1234,
            username = "username",
            email = "email@google.com",
            firstName = "firstname",
            lastName = "lastname",
            gender = "male",
            image = "https://robohash.org/doloremquesintcorrupti.png",
            token = "1234567890123456789012345678901234567890",
        )
}