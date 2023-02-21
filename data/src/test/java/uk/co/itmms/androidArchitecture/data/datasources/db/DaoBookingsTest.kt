package uk.co.itmms.androidArchitecture.data.datasources.db

import uk.co.itmms.androidArchitecture.data.BaseDataDaoTest
import uk.co.itmms.androidArchitecture.data.models.DBBooking
import uk.co.itmms.androidArchitecture.data.models.DBBookingStatus
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class DaoBookingsTest : BaseDataDaoTest() {

    @Test
    fun `testing insert and count`() = runBlocking(Dispatchers.IO) {
        val booking = createBooking(1)

        daoBookings.insert(booking)
        val actual = daoBookings.count()

        assertEquals(1, actual)
    }

    @Test
    fun `testing insert list and count`() = runBlocking(Dispatchers.IO) {
        val bookingList = listOf(
            createBooking(1),
            createBooking(2),
        )

        daoBookings.insert(bookingList)
        val actual = daoBookings.count()

        assertEquals(bookingList.size, actual)
    }

    @Test
    fun `testing list`() = runBlocking(Dispatchers.IO) {
        val booking1 = createBooking(1)
        val booking2 = createBooking(2)
        val bookingList = listOf(
            booking1,
            booking2,
        )

        daoBookings.insert(bookingList)
        val actual = daoBookings.list()

        assertEquals(2, actual.size)
        assertEquals(booking1.id, actual[0].id)
        assertEquals(booking2.id, actual[1].id)
    }

    @Test
    fun `testing getById with existing and not existing id`() = runBlocking(Dispatchers.IO) {
        val booking = createBooking(1)

        daoBookings.insert(booking)
        var actual = daoBookings.getById(booking.id)

        assertNotNull(actual)

        actual = daoBookings.getById(999999)

        assertNull(actual)
    }

    @Test
    fun `testing update with existing booking`() = runBlocking(Dispatchers.IO) {
        val booking = createBooking(1)
        val newStatus = DBBookingStatus.Unknown

        daoBookings.insert(booking)
        val dbBooking = daoBookings.getById(booking.id)!!.copy(status = newStatus)
        daoBookings.update(dbBooking)

        assertEquals(1, daoBookings.count())
        assertEquals(newStatus, daoBookings.getById(booking.id)!!.status)
    }

    @Test
    fun `testing update with NOT existing booking`() = runBlocking(Dispatchers.IO) {
        val booking = createBooking(1)

        daoBookings.update(booking)

        assertEquals(0, daoBookings.count())
        assertNull(daoBookings.getById(booking.id))
    }

    @Test
    fun `test delete single`() = runBlocking(Dispatchers.IO) {
        val booking = createBooking(3)
        val bookingList = listOf(
            createBooking(1),
            createBooking(2),
            booking,
            createBooking(4),
        )

        daoBookings.insert(bookingList)
        daoBookings.delete(booking)

        assertEquals(bookingList.size - 1, daoBookings.count())
        assertNull(daoBookings.getById(booking.id))
    }

    @Test
    fun `test delete multiple`() = runBlocking(Dispatchers.IO) {
        val booking1 = createBooking(1)
        val booking3 = createBooking(3)
        val bookingList = listOf(
            booking1,
            createBooking(2),
            booking3,
            createBooking(4),
        )
        val bookingToDeleteList = listOf(
            booking1,
            booking3
        )

        daoBookings.insert(bookingList)
        daoBookings.delete(bookingToDeleteList)

        assertEquals(bookingList.size - bookingToDeleteList.size, daoBookings.count())
        assertNull(daoBookings.getById(booking1.id))
        assertNull(daoBookings.getById(booking3.id))
    }

    private fun createBooking(id: Long): DBBooking =
        DBBooking(
            id,
            "customer $id",
            DBBookingStatus.Started,
            start = Date(),
            end = Date(),
        )
}