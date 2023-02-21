package uk.co.itmms.androidArchitecture.data.models

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.*

class DBCustomerTest {

    private val title = "Mr"

    private val firstname = "John"

    @Test
    fun `testing name property`() {
        var actual = createCustomer(title = title, firstname = firstname).name

        assertEquals("Mr John", actual)

        actual = createCustomer(title = title, firstname = null).name

        assertEquals("Mr", actual)

        actual = createCustomer(title = null, firstname = firstname).name

        assertEquals("John", actual)

        actual = createCustomer(title = null, firstname = null).name

        assertEquals("", actual)
    }

    private fun createCustomer(title: String?, firstname: String?): DBCustomer =
        DBCustomer(
            bid = "customerBid",
            uuid = UUID.randomUUID().toString(),
            title = title,
            firstname = firstname,
            location = null,
        )
}