package uk.co.itmms.androidArchitecture.data.datasources.db

import uk.co.itmms.androidArchitecture.data.BaseDataDaoTest
import uk.co.itmms.androidArchitecture.data.models.DBCustomer
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class DaoCustomersTest : BaseDataDaoTest() {

    @Test
    fun `testing insert and count`() = runBlocking(Dispatchers.IO) {
        val customer = createCustomer("1")

        daoCustomers.insert(customer)
        val actual = daoCustomers.count()

        assertEquals(1, actual)
    }

    @Test
    fun `testing insert list and count`() = runBlocking(Dispatchers.IO) {
        val customerList = listOf(
            createCustomer("1"),
            createCustomer("2"),
        )

        daoCustomers.insert(customerList)
        val actual = daoCustomers.count()

        assertEquals(customerList.size, actual)
    }

    @Test
    fun `testing list`() = runBlocking(Dispatchers.IO) {
        val customer1 = createCustomer("1")
        val customer2 = createCustomer("2")
        val customerList = listOf(
            customer1,
            customer2,
        )

        daoCustomers.insert(customerList)
        val actual = daoCustomers.list()

        assertEquals(2, actual.size)
        assertEquals(customer1.bid, actual[0].bid)
        assertEquals(customer2.bid, actual[1].bid)
    }

    @Test
    fun `testing getByBid with existing and not existing id`() = runBlocking(Dispatchers.IO) {
        val customer = createCustomer("1")

        daoCustomers.insert(customer)
        var actual = daoCustomers.getByBid(customer.bid)

        assertNotNull(actual)

        actual = daoCustomers.getByBid("not existing")

        assertNull(actual)
    }

    @Test
    fun `testing update with existing Customer`() = runBlocking(Dispatchers.IO) {
        val customer = createCustomer("1")
        val newTitle = "Mr"

        daoCustomers.insert(customer)
        val dbCustomer = daoCustomers.getByBid(customer.bid)!!.copy(title = newTitle)
        daoCustomers.update(dbCustomer)

        assertEquals(1, daoCustomers.count())
        assertEquals(newTitle, daoCustomers.getByBid(customer.bid)!!.title)
    }

    @Test
    fun `testing update with NOT existing Customer`() = runBlocking(Dispatchers.IO) {
        val customer = createCustomer("1")

        daoCustomers.update(customer)

        assertEquals(0, daoCustomers.count())
        assertNull(daoCustomers.getByBid(customer.bid))
    }

    @Test
    fun `test delete single`() = runBlocking(Dispatchers.IO) {
        val customer = createCustomer("3")
        val customerList = listOf(
            createCustomer("1"),
            createCustomer("2"),
            customer,
            createCustomer("4"),
        )

        daoCustomers.insert(customerList)
        daoCustomers.delete(customer)

        assertEquals(customerList.size-1, daoCustomers.count())
        assertNull(daoCustomers.getByBid(customer.bid))
    }

    @Test
    fun `test delete multiple`() = runBlocking(Dispatchers.IO) {
        val customer1 = createCustomer("1")
        val customer3 = createCustomer("3")
        val customerList = listOf(
            customer1,
            createCustomer("2"),
            customer3,
            createCustomer("4"),
        )
        val customerToDeleteList = listOf(
            customer1,
            customer3
        )

        daoCustomers.insert(customerList)
        daoCustomers.delete(customerToDeleteList)

        assertEquals(customerList.size - customerToDeleteList.size, daoCustomers.count())
        assertNull(daoCustomers.getByBid(customer1.bid))
        assertNull(daoCustomers.getByBid(customer3.bid))
    }

    private fun createCustomer(bid: String): DBCustomer =
        DBCustomer(
            bid = bid,
            uuid = UUID.randomUUID().toString(),
            title = null,
            firstname = "name $bid",
            location = null,
        )
}