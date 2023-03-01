package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.data.datasources.db.DaoBookings
import uk.co.itmms.androidArchitecture.data.datasources.db.IAppDatabase
import uk.co.itmms.androidArchitecture.data.models.DBBookingStatus
import uk.co.itmms.androidArchitecture.data.models.DBBooking
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataSourceDatabaseBookingsTest: BaseDataTest() {

    @MockK
    private lateinit var mockPassDatabase: IAppDatabase

    @MockK
    private lateinit var mockDaoBookings: DaoBookings

    private lateinit var dataSourceDatabaseBooking: DataSourceDatabaseBookings

    @Before
    fun setUp() {
        dataSourceDatabaseBooking = DataSourceDatabaseBookings(
            mockPassDatabase
        )

        coEvery { mockPassDatabase.daoBookings() } returns mockDaoBookings
    }

    @Test
    fun `testing list`() = runBlocking {
        val mockList = listOf<DBBooking>(mockk(), mockk())
        coEvery { mockDaoBookings.list() } returns mockList

        val actual = dataSourceDatabaseBooking.list()

        TestCase.assertEquals(mockList, actual)
        coVerify(exactly = 1) {
            mockDaoBookings.list()
        }
        confirmVerified(mockDaoBookings)
    }

    @Test
    fun `testing listByTitle`() = runBlocking {
        val status = DBBookingStatus.Completed
        val mockBooking1 = mockk<DBBooking>()
        val mockBooking2 = mockk<DBBooking>()
        val mockBooking3 = mockk<DBBooking>()
        coEvery { mockBooking1.status } returns DBBookingStatus.Scheduled
        coEvery { mockBooking2.status } returns DBBookingStatus.Scheduled
        coEvery { mockBooking3.status } returns status
        coEvery { mockDaoBookings.list() } returns listOf(mockBooking1, mockBooking2, mockBooking3)

        val actual = dataSourceDatabaseBooking.listByStatus(status)

        TestCase.assertEquals(1, actual.size)
        coVerify(exactly = 1) {
            mockDaoBookings.list()
        }
        confirmVerified(mockDaoBookings)
    }

    @Test
    fun `testing insert`() = runBlocking {
        val mockBooking = mockk<DBBooking>()

        dataSourceDatabaseBooking.insert(mockBooking)

        coVerify(exactly = 1) {
            mockDaoBookings.insert(mockBooking)
        }
        confirmVerified(mockDaoBookings)
    }

    @Test
    fun `testing insert list`() = runBlocking {
        val mockList = listOf<DBBooking>(mockk(), mockk())

        dataSourceDatabaseBooking.insert(mockList)

        coVerify(exactly = 1) {
            mockDaoBookings.insert(mockList)
        }
        confirmVerified(mockDaoBookings)
    }

    @Test
    fun `testing update`() = runBlocking {
        val mockBooking = mockk<DBBooking>()

        dataSourceDatabaseBooking.update(mockBooking)

        coVerify(exactly = 1) {
            mockDaoBookings.update(mockBooking)
        }
        confirmVerified(mockDaoBookings)
    }

    @Test
    fun `testing update list`() = runBlocking {
        val mockBooking1 = mockk<DBBooking>()
        val mockBooking2 = mockk<DBBooking>()
        val mockList = listOf(mockBooking1, mockBooking2)

        dataSourceDatabaseBooking.update(mockList)

        coVerify(exactly = 1) {
            mockDaoBookings.update(mockBooking1)
            mockDaoBookings.update(mockBooking2)
        }
        confirmVerified(mockDaoBookings)
    }

    @Test
    fun `testing delete`() = runBlocking {
        val mockBooking = mockk<DBBooking>()

        dataSourceDatabaseBooking.delete(mockBooking)

        coVerify(exactly = 1) {
            mockDaoBookings.delete(mockBooking)
        }
        confirmVerified(mockDaoBookings)
    }

    @Test
    fun `testing deleteAll`() = runBlocking {
        val mockBooking1 = mockk<DBBooking>()
        val mockBooking2 = mockk<DBBooking>()
        val mockList = listOf(mockBooking1, mockBooking2)

        coEvery { mockDaoBookings.list() } returns mockList

        dataSourceDatabaseBooking.deleteAll()

        coVerify(exactly = 1) {
            mockDaoBookings.list()
            mockDaoBookings.delete(mockBooking1)
            mockDaoBookings.delete(mockBooking2)
        }
    }
}