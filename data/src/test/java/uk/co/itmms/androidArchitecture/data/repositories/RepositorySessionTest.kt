package uk.co.itmms.androidArchitecture.data.repositories

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import java.util.Date

class RepositorySessionTest : BaseDataTest() {

    private lateinit var repositorySession: RepositorySession

    @Before
    fun setUp() {
        repositorySession = RepositorySession()
    }

    @Test
    fun `testing officeBid`() = runBlocking {
        val officeBid = "officeBid"

        assertTrue(repositorySession.officeBid.isEmpty())

        repositorySession.officeBid = officeBid

        assertEquals(officeBid, repositorySession.officeBid)

        repositorySession.clear()

        assertTrue(repositorySession.officeBid.isEmpty())
    }

    @Test
    fun `testing customerList`() = runBlocking {
        val customerList = listOf(
            Customer("1", "name 1", "address 1"),
            Customer("2", "name 2", "address 2")
        )

        assertTrue(repositorySession.customerList.isEmpty())

        repositorySession.customerList = customerList

        assertEquals(customerList, repositorySession.customerList)

        repositorySession.clear()

        assertTrue(repositorySession.customerList.isEmpty())
    }

    @Test
    fun `testing bookingList`() = runBlocking {
        val bookingList = listOf(
            Booking(1, "customer 1", BookingStatus.Started, Date(), Date()),
        )

        assertTrue(repositorySession.bookingList.isEmpty())

        repositorySession.bookingList = bookingList

        assertEquals(bookingList, repositorySession.bookingList)

        repositorySession.clear()

        assertTrue(repositorySession.bookingList.isEmpty())
    }
}