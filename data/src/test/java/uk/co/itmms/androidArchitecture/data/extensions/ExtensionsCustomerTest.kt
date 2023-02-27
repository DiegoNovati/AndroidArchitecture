package uk.co.itmms.androidArchitecture.data.extensions

//import uk.co.itmms.androidArchitecture.data.models.DBCustomer
//import uk.co.itmms.androidArchitecture.data.models.NetCustomer
//import junit.framework.TestCase.assertEquals
//import org.junit.Test
//import java.util.*
//
//class ExtensionsCustomerTest {
//
//    private val bid = "customerBid"
//    private val uuid = UUID.randomUUID().toString()
//    private val title = "Mr"
//    private val firstname = "John"
//    private val location = "14 Oxford Circus"
//
//    @Test
//    fun `testing toCustomer`() {
//        var actual = createDBCustomer().toCustomer()
//
//        assertEquals(bid, actual.customerBid)
//        assertEquals("Mr John", actual.name)
//        assertEquals(location, actual.address)
//
//        actual = createDBCustomer(locationValue = null).toCustomer()
//
//        assertEquals(bid, actual.customerBid)
//        assertEquals("Mr John", actual.name)
//        assertEquals("", actual.address)
//    }
//
//    @Test
//    fun `testing toCustomerList`() {
//        val bid2 = "customerBid2"
//        val dbCustomerList = listOf(
//            createDBCustomer(),
//            createDBCustomer(bidValue = bid2)
//        )
//
//        val actual = dbCustomerList.toCustomerList()
//
//        assertEquals(dbCustomerList.size, actual.size)
//        assertEquals(bid, actual[0].customerBid)
//        assertEquals(bid2, actual[1].customerBid)
//    }
//
//    @Test
//    fun `testing toDBCustomerList`() {
//        val bid2 = "customerBid2"
//        val netCustomerList = listOf(
//            createNetCustomer(bid),
//            createNetCustomer(bid2),
//        )
//
//        val actual = netCustomerList.toDBCustomerList()
//
//        assertEquals(netCustomerList.size, actual.size)
//        assertEquals(bid, actual[0].bid)
//        assertEquals(bid2, actual[1].bid)
//    }
//
//    private fun createDBCustomer(bidValue: String = bid, uuidValue: String = uuid,
//                                 titleValue: String? = title, firstnameValue: String? = firstname,
//                                 locationValue: String? = location): DBCustomer =
//        DBCustomer(
//            bid = bidValue,
//            uuid = uuidValue,
//            title = titleValue,
//            firstname = firstnameValue,
//            location = locationValue
//        )
//
//    private fun createNetCustomer(bid: String): NetCustomer =
//        NetCustomer(
//            id = 1,
//            bid = bid,
//            uuid = UUID.randomUUID().toString(),
//            title = "Mr",
//            firstname = "firstname",
//            nickname = "nickname",
//            surname = "surname",
//            location = "location",
//            status = "status",
//            dnr = true,
//            dols = false,
//            dob = "12-03-2021T14:13:12",
//            allergies = false,
//            modified = Date(),
//            photoKey = null,
//            careplanReviewDate = "",
//        )
//}