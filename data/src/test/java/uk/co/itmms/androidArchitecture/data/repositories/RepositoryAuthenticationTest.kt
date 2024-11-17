package uk.co.itmms.androidArchitecture.data.repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.network.NetworkApiErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.NetworkApiException
import uk.co.itmms.androidArchitecture.data.models.NetAuthenticateOffice
import uk.co.itmms.androidArchitecture.data.models.NetAuthenticateOfficeCdn
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication

class RepositoryAuthenticationTest : BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceBackend: IDataSourceBackend

    @InjectMockKs
    private lateinit var repositoryAuthentication: RepositoryAuthentication

    private val username = "username"
    private val password = "password"
    private val officeBid = "officeBid"

    @After
    fun tearDown() {
        confirmVerified(mockDataSourceBackend)
    }

    @Test
    fun `WHEN login is successful THEN returns a right value`() = runBlocking {
        val netAuthenticateOfficeList = listOf(
            createNetAuthenticateOffice(true)
        )

        coEvery { mockDataSourceBackend.authenticate(any(), any()) } returns netAuthenticateOfficeList

        val actual = repositoryAuthentication.login(username, password)

        assertTrue(actual.isRight())
        actual.fold({
            fail("Unexpected failure")
        }){ result ->
            assertEquals(officeBid, result.officeBid)
        }

        coVerify(exactly = 1) {
            mockDataSourceBackend.authenticate(username, password)
        }
    }

    @Test
    fun `WHEN login has no offices THEN returns a left value`() = runBlocking {
        val netAuthenticateOfficeList = listOf<NetAuthenticateOffice>()

        coEvery { mockDataSourceBackend.authenticate(any(), any()) } returns netAuthenticateOfficeList

        val actual = repositoryAuthentication.login(username, password)

        assertTrue(actual.isLeft())
        actual.fold({
            assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError, it)
        }){
            fail("Unexpected success")
        }

        coVerify(exactly = 1) {
            mockDataSourceBackend.authenticate(username, password)
        }
    }

    @Test
    fun `WHEN login returns v1 THEN returns a left value`() = runBlocking {
        val netAuthenticateOfficeList = listOf(
            createNetAuthenticateOffice(false)
        )

        coEvery { mockDataSourceBackend.authenticate(any(), any()) } returns netAuthenticateOfficeList

        val actual = repositoryAuthentication.login(username, password)

        assertTrue(actual.isLeft())
        actual.fold({
            assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError, it)
        }){
            fail("Unexpected success")
        }

        coVerify(exactly = 1) {
            mockDataSourceBackend.authenticate(username, password)
        }
    }

    @Test
    fun `testing logout`() = runBlocking {
        repositoryAuthentication.logout()

        coVerify {
            mockDataSourceBackend.logout()
        }
    }

    @Test
    fun `testing toRepositoryAuthenticationFailure`() {
        var actual = NetworkApiException(errorCode = NetworkApiErrorCode.UnknownHost).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)

        actual = NetworkApiException(errorCode = NetworkApiErrorCode.Timeout).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)

        actual = NetworkApiException(errorCode = NetworkApiErrorCode.SSLError).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)

        actual = NetworkApiException(errorCode = NetworkApiErrorCode.HttpUnmanaged).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)

        actual = NetworkApiException(errorCode = NetworkApiErrorCode.IO).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)

        actual = NetworkApiException(errorCode = NetworkApiErrorCode.Http400).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems, actual)

        actual = NetworkApiException(errorCode = NetworkApiErrorCode.Http401).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError, actual)

        actual = NetworkApiException(errorCode = NetworkApiErrorCode.Http403).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems, actual)

        actual = NetworkApiException(errorCode = NetworkApiErrorCode.NoDataChanges).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems, actual)

        actual = NetworkApiException(errorCode = NetworkApiErrorCode.Unexpected).toRepositoryAuthenticationFailure()

        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems, actual)
    }

    private fun createNetAuthenticateOffice(v2: Boolean): NetAuthenticateOffice =
        NetAuthenticateOffice(
            bid = officeBid,
            display = "display",
            v2 = v2,
            cdn = NetAuthenticateOfficeCdn(
                baseUrl = "baseUrl",
                policy = "policy",
                signature = "signature",
                keyPairId = "keyPairId",
            )
        )
}