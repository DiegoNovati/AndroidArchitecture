package uk.co.itmms.androidArchitecture.data.repositories

import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositorySession

class RepositorySession : IRepositorySession {

    private var _officeBid: String = ""
    private var _customerList: List<Customer> = emptyList()
    private var _bookingList: List<Booking> = emptyList()

    override suspend fun clear() {
        officeBid = ""
        customerList = emptyList()
        bookingList = emptyList()
    }

    override var officeBid: String
        get() = _officeBid
        set(value) { _officeBid = value }

    override var customerList: List<Customer>
        get() = _customerList
        set(value) { _customerList = value }

    override var bookingList: List<Booking>
        get() = _bookingList
        set(value) { _bookingList = value }
}