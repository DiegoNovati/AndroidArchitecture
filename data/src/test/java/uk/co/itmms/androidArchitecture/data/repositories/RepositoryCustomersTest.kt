package uk.co.itmms.androidArchitecture.data.repositories

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseCustomers
import uk.co.itmms.androidArchitecture.data.datasources.network.PassApiErrorCode
import uk.co.itmms.androidArchitecture.data.datasources.network.PassApiException
import uk.co.itmms.androidArchitecture.data.extensions.toCustomerList
import uk.co.itmms.androidArchitecture.data.extensions.toDBCustomerList
import uk.co.itmms.androidArchitecture.data.models.DBCustomer
import uk.co.itmms.androidArchitecture.data.models.NetCustomer
import uk.co.itmms.androidArchitecture.data.models.NetCustomersResponse
import uk.co.itmms.androidArchitecture.domain.repositories.RepositoryBackendFailure
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class RepositoryCustomersTest : BaseDataTest() {

    @MockK
    private lateinit var mockDataSourceBackend: IDataSourceBackend

    @MockK
    private lateinit var mockDataSourceDatabaseCustomers: IDataSourceDatabaseCustomers

    private lateinit var repositoryCustomers: RepositoryCustomers

    private val officeBid = "officeBid"

    @Before
    fun setUp() {
        repositoryCustomers = RepositoryCustomers(
            mockDataSourceBackend, mockDataSourceDatabaseCustomers,
        )
    }

    @Test
    fun `WHEN the regular path is successful THEN it returns a right value`() = runBlocking {
        val netCustomersResponse = NetCustomersResponse(
            customers = listOf(
                createNetCustomer(1),
                createNetCustomer(2),
            )
        )
        coEvery { mockDataSourceBackend.getCustomerList(any()) } returns netCustomersResponse

        val actual = repositoryCustomers.getCustomerList(officeBid)

        assertTrue(actual.isRight())
        actual.fold({}){
            assertEquals(netCustomersResponse.customers.toDBCustomerList().toCustomerList(), it)
        }

        coVerify(exactly = 1) {
            mockDataSourceBackend.getCustomerList(officeBid)
            mockDataSourceDatabaseCustomers.deleteAll()
            mockDataSourceDatabaseCustomers.insert(any<List<DBCustomer>>())
        }
        confirmVerified(mockDataSourceBackend)
        confirmVerified(mockDataSourceDatabaseCustomers)
    }

    @Test
    fun `WHEN the regular path fails THEN it returns a left value`() = runBlocking {
        coEvery { mockDataSourceBackend.getCustomerList(any()) } throws PassApiException(errorCode = PassApiErrorCode.SSLError)

        val actual = repositoryCustomers.getCustomerList(officeBid)

        assertTrue(actual.isLeft())
        actual.fold({
            assertEquals(RepositoryBackendFailure.ConnectionProblems, it)
        }){}

        coVerify(exactly = 1) {
            mockDataSourceBackend.getCustomerList(officeBid)
        }
        confirmVerified(mockDataSourceBackend)
        confirmVerified(mockDataSourceDatabaseCustomers)
    }

    @Test
    fun `WHEN the NoDataChanges path is successful THEN it returns a right value`() = runBlocking {
        val dbCustomers = listOf(
            createDBCustomer("bid1"),
            createDBCustomer("bid2"),
        )
        coEvery { mockDataSourceBackend.getCustomerList(any()) } throws PassApiException(errorCode = PassApiErrorCode.NoDataChanges)
        coEvery { mockDataSourceDatabaseCustomers.list() } returns dbCustomers

        val actual = repositoryCustomers.getCustomerList(officeBid)

        assertTrue(actual.isRight())
        actual.fold({}){
            assertEquals(dbCustomers.toCustomerList(), it)
        }

        coVerify(exactly = 1) {
            mockDataSourceBackend.getCustomerList(officeBid)
            mockDataSourceDatabaseCustomers.list()
        }
        confirmVerified(mockDataSourceBackend)
        confirmVerified(mockDataSourceDatabaseCustomers)
    }

    private fun createNetCustomer(id: Long): NetCustomer =
        NetCustomer(
            id = id,
            bid = "customerBid$id",
            uuid = UUID.randomUUID().toString(),
            title = "title$id",
            firstname = "firstname$id",
            nickname = "nickname$id",
            surname = "surname$id",
            location = "location$id",
            status = "SCHEDULED",
            dnr = false,
            dols = true,
            dob = "21-02-2022T12:13:14",
            allergies = false,
            modified = Date(),
            photoKey = null,
            careplanReviewDate = "",
        )

    private fun createDBCustomer(bid: String): DBCustomer =
        DBCustomer(
            bid = bid,
            uuid = UUID.randomUUID().toString(),
            title = null,
            firstname = null,
            location = null,
        )
}