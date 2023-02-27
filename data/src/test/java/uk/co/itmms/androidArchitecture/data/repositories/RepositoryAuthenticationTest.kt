package uk.co.itmms.androidArchitecture.data.repositories

//import uk.co.itmms.androidArchitecture.data.BaseDataTest
//import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
//import uk.co.itmms.androidArchitecture.data.datasources.network.BackendErrorCode
//import uk.co.itmms.androidArchitecture.data.datasources.network.BackendException
//import uk.co.itmms.androidArchitecture.data.models.NetAuthenticateOffice
//import uk.co.itmms.androidArchitecture.data.models.NetAuthenticateOfficeCdn
//import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryAuthentication
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.confirmVerified
//import io.mockk.impl.annotations.MockK
//import junit.framework.TestCase.assertEquals
//import junit.framework.TestCase.assertTrue
//import kotlinx.coroutines.runBlocking
//import org.junit.Before
//import org.junit.Test
//
//class RepositoryAuthenticationTest : BaseDataTest() {
//
//    @MockK
//    private lateinit var mockDataSourceBackend: IDataSourceBackend
//
//    private lateinit var repositoryAuthentication: RepositoryAuthentication
//
//    private val username = "username"
//
//    private val password = "password"
//
//    private val officeBid = "officeBid"
//
//    @Before
//    fun setUp() {
//        repositoryAuthentication = RepositoryAuthentication(
//            mockDataSourceBackend
//        )
//    }
//
//    @Test
//    fun `WHEN login is successful THEN returns a right value`() = runBlocking {
//        val netAuthenticateOfficeList = listOf(
//            createNetAuthenticateOffice(true)
//        )
//
//        coEvery { mockDataSourceBackend.authenticate(any(), any()) } returns netAuthenticateOfficeList
//
//        val actual = repositoryAuthentication.login(username, password)
//
//        assertTrue(actual.isRight())
//        actual.fold({}){
//            assertEquals(officeBid, it.officeBid)
//        }
//
//        coVerify(exactly = 1) {
//            mockDataSourceBackend.authenticate(username, password)
//        }
//        confirmVerified(mockDataSourceBackend)
//    }
//
//    @Test
//    fun `WHEN login has no offices THEN returns a left value`() = runBlocking {
//        val netAuthenticateOfficeList = listOf<NetAuthenticateOffice>()
//
//        coEvery { mockDataSourceBackend.authenticate(any(), any()) } returns netAuthenticateOfficeList
//
//        val actual = repositoryAuthentication.login(username, password)
//
//        assertTrue(actual.isLeft())
//        actual.fold({
//            assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError, it)
//        }){}
//
//        coVerify(exactly = 1) {
//            mockDataSourceBackend.authenticate(username, password)
//        }
//        confirmVerified(mockDataSourceBackend)
//    }
//
//    @Test
//    fun `WHEN login returns v1 THEN returns a left value`() = runBlocking {
//        val netAuthenticateOfficeList = listOf(
//            createNetAuthenticateOffice(false)
//        )
//
//        coEvery { mockDataSourceBackend.authenticate(any(), any()) } returns netAuthenticateOfficeList
//
//        val actual = repositoryAuthentication.login(username, password)
//
//        assertTrue(actual.isLeft())
//        actual.fold({
//            assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError, it)
//        }){}
//
//        coVerify(exactly = 1) {
//            mockDataSourceBackend.authenticate(username, password)
//        }
//        confirmVerified(mockDataSourceBackend)
//    }
//
//    @Test
//    fun `testing logout`() = runBlocking {
//        repositoryAuthentication.logout()
//
//        coVerify {
//            mockDataSourceBackend.logout()
//        }
//        confirmVerified(mockDataSourceBackend)
//    }
//
//    @Test
//    fun `testing toRepositoryAuthenticationFailure`() {
//        var actual = BackendException(errorCode = BackendErrorCode.UnknownHost).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)
//
//        actual = BackendException(errorCode = BackendErrorCode.Timeout).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)
//
//        actual = BackendException(errorCode = BackendErrorCode.SSLError).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)
//
//        actual = BackendException(errorCode = BackendErrorCode.HttpUnmanaged).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)
//
//        actual = BackendException(errorCode = BackendErrorCode.IO).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.ConnectionProblems, actual)
//
//        actual = BackendException(errorCode = BackendErrorCode.Http400).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems, actual)
//
//        actual = BackendException(errorCode = BackendErrorCode.Http401).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.LoginError, actual)
//
//        actual = BackendException(errorCode = BackendErrorCode.Http403).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems, actual)
//
//        actual = BackendException(errorCode = BackendErrorCode.NoDataChanges).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems, actual)
//
//        actual = BackendException(errorCode = BackendErrorCode.Unexpected).toRepositoryAuthenticationFailure()
//
//        assertEquals(IRepositoryAuthentication.RepositoryAuthenticationFailure.BackendProblems, actual)
//    }
//
//    private fun createNetAuthenticateOffice(v2: Boolean): NetAuthenticateOffice =
//        NetAuthenticateOffice(
//            bid = officeBid,
//            display = "display",
//            v2 = v2,
//            cdn = NetAuthenticateOfficeCdn(
//                baseUrl = "baseUrl",
//                policy = "policy",
//                signature = "signature",
//                keyPairId = "keyPairId",
//            )
//        )
//}