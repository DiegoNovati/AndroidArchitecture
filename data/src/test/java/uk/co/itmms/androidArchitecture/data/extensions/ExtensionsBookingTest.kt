package uk.co.itmms.androidArchitecture.data.extensions

//import uk.co.itmms.androidArchitecture.data.models.DBBooking
//import uk.co.itmms.androidArchitecture.data.models.DBBookingStatus
//import uk.co.itmms.androidArchitecture.data.models.NetBooking
//import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
//import junit.framework.TestCase.assertEquals
//import junit.framework.TestCase.fail
//import org.junit.Test
//import java.util.*
//
//class ExtensionsBookingTest {
//
//    @Test
//    fun `testing toBooking`() {
//        var actual = createDBBooking(1, DBBookingStatus.Scheduled).toBooking()
//
//        assertEquals(BookingStatus.Scheduled, actual.status)
//
//        actual = createDBBooking(1, DBBookingStatus.Started).toBooking()
//
//        assertEquals(BookingStatus.Started, actual.status)
//
//        actual = createDBBooking(1, DBBookingStatus.Completed).toBooking()
//
//        assertEquals(BookingStatus.Completed, actual.status)
//
//        try {
//            createDBBooking(1, DBBookingStatus.Unknown).toBooking()
//            fail("Trying to convert a DBBooking with an Unknown status must raise a InvalidPropertiesFormatException")
//        } catch (_: InvalidPropertiesFormatException) {}
//    }
//
//    @Test
//    fun `testing toBookingList`() {
//        val dbBookingList = listOf(
//            createDBBooking(1, DBBookingStatus.Scheduled),
//            createDBBooking(2, DBBookingStatus.Started),
//            createDBBooking(3, DBBookingStatus.Unknown),
//            createDBBooking(4, DBBookingStatus.Completed),
//        )
//
//        val actual = dbBookingList.toBookingList()
//
//        assertEquals(dbBookingList.size-1, actual.size)
//        assertEquals(1, actual[0].bookingBid)
//        assertEquals(2, actual[1].bookingBid)
//        assertEquals(4, actual[2].bookingBid)
//    }
//
//    @Test
//    fun `testing toDBBooking`() {
//        var actual = createNetBooking("SCHEDULED").toDBBooking()
//
//        assertEquals(DBBookingStatus.Scheduled, actual.status)
//
//        actual = createNetBooking("STARTED").toDBBooking()
//
//        assertEquals(DBBookingStatus.Started, actual.status)
//
//        actual = createNetBooking("COMPLETED").toDBBooking()
//
//        assertEquals(DBBookingStatus.Completed, actual.status)
//
//        actual = createNetBooking("UNKNOWN").toDBBooking()
//
//        assertEquals(DBBookingStatus.Unknown, actual.status)
//
//        actual = createNetBooking("ANYTHING ELSE").toDBBooking()
//
//        assertEquals(DBBookingStatus.Unknown, actual.status)
//    }
//
//    @Test
//    fun `testing toDBBookingList`() {
//        val netBookingList = listOf(
//            createNetBooking("STARTED"),
//            createNetBooking("SCHEDULED"),
//        )
//
//        val actual = netBookingList.toDBBookingList()
//
//        assertEquals(netBookingList.size, actual.size)
//        assertEquals(DBBookingStatus.Started, actual[0].status)
//        assertEquals(DBBookingStatus.Scheduled, actual[1].status)
//    }
//
//    private fun createNetBooking(status: String): NetBooking =
//        NetBooking(
//            id = 1,
//            customerBid = "customerBid",
//            status = status,
//            nfcTag = 0,
//            otherCareworkers = listOf(),
//            start = Date(),
//            end = Date(),
//        )
//
//    private fun createDBBooking(id: Long, status: DBBookingStatus): DBBooking =
//        DBBooking(
//            id = id,
//            customerBid = "customerBid",
//            status = status,
//            start = Date(),
//            end = Date(),
//        )
//}