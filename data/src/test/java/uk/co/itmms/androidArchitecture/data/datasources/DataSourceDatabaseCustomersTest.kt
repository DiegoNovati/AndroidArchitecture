package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.db.DaoCustomers
import uk.co.itmms.androidArchitecture.data.datasources.db.IAppDatabase
import uk.co.itmms.androidArchitecture.data.models.DBCustomer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceDatabaseCustomersTest: BaseDataTest() {

    @MockK
    private lateinit var mockPassDatabase: IAppDatabase

    @MockK
    private lateinit var mockDaoCustomers: DaoCustomers

    private lateinit var dataSourceDatabaseCustomers: DataSourceDatabaseCustomers

    @Before
    fun setUp() {
        dataSourceDatabaseCustomers = DataSourceDatabaseCustomers(
            mockPassDatabase
        )

        coEvery { mockPassDatabase.daoCustomers() } returns mockDaoCustomers
    }

    @Test
    fun `testing list`() = runBlocking {
        val mockList = listOf<DBCustomer>(mockk(), mockk())
        coEvery { mockDaoCustomers.list() } returns mockList

        val actual = dataSourceDatabaseCustomers.list()

        assertEquals(mockList, actual)
        coVerify(exactly = 1) {
            mockDaoCustomers.list()
        }
        confirmVerified(mockDaoCustomers)
    }

    @Test
    fun `testing listByTitle`() = runBlocking {
        val title = "Mr"
        val mockCustomer1 = mockk<DBCustomer>()
        val mockCustomer2 = mockk<DBCustomer>()
        val mockCustomer3 = mockk<DBCustomer>()
        coEvery { mockCustomer1.title } returns "__"
        coEvery { mockCustomer2.title } returns "__another__"
        coEvery { mockCustomer3.title } returns title
        coEvery { mockDaoCustomers.list() } returns listOf(mockCustomer1, mockCustomer2, mockCustomer3)

        val actual = dataSourceDatabaseCustomers.listByTitle(title)

        assertEquals(1, actual.size)
        coVerify(exactly = 1) {
            mockDaoCustomers.list()
        }
        confirmVerified(mockDaoCustomers)
    }

    @Test
    fun `testing insert`() = runBlocking {
        val mockCustomer = mockk<DBCustomer>()

        dataSourceDatabaseCustomers.insert(mockCustomer)

        coVerify(exactly = 1) {
            mockDaoCustomers.insert(mockCustomer)
        }
        confirmVerified(mockDaoCustomers)
    }

    @Test
    fun `testing insert list`() = runBlocking {
        val mockList = listOf<DBCustomer>(mockk(), mockk())

        dataSourceDatabaseCustomers.insert(mockList)

        coVerify(exactly = 1) {
            mockDaoCustomers.insert(mockList)
        }
        confirmVerified(mockDaoCustomers)
    }

    @Test
    fun `testing update`() = runBlocking {
        val mockCustomer = mockk<DBCustomer>()

        dataSourceDatabaseCustomers.update(mockCustomer)

        coVerify(exactly = 1) {
            mockDaoCustomers.update(mockCustomer)
        }
        confirmVerified(mockDaoCustomers)
    }

    @Test
    fun `testing update list`() = runBlocking {
        val mockCustomer1 = mockk<DBCustomer>()
        val mockCustomer2 = mockk<DBCustomer>()
        val mockList = listOf(mockCustomer1, mockCustomer2)

        dataSourceDatabaseCustomers.update(mockList)

        coVerify(exactly = 1) {
            mockDaoCustomers.update(mockCustomer1)
            mockDaoCustomers.update(mockCustomer2)
        }
        confirmVerified(mockDaoCustomers)
    }

    @Test
    fun `testing delete`() = runBlocking {
        val mockCustomer = mockk<DBCustomer>()

        dataSourceDatabaseCustomers.delete(mockCustomer)

        coVerify(exactly = 1) {
            mockDaoCustomers.delete(mockCustomer)
        }
        confirmVerified(mockDaoCustomers)
    }

    @Test
    fun `testing deleteAll`() = runBlocking {
        val mockCustomer1 = mockk<DBCustomer>()
        val mockCustomer2 = mockk<DBCustomer>()
        val mockList = listOf(mockCustomer1, mockCustomer2)

        coEvery { mockDaoCustomers.list() } returns mockList

        dataSourceDatabaseCustomers.deleteAll()

        coVerify(exactly = 1) {
            mockDaoCustomers.list()
            mockDaoCustomers.delete(mockCustomer1)
            mockDaoCustomers.delete(mockCustomer2)
        }
    }
}