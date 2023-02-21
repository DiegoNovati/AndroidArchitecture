package uk.co.itmms.androidArchitecture.data.repositories

import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class RepositoryRuntime : IRepositoryRuntime {
    private val _authenticated = MutableStateFlow(false)
    private val authenticated: StateFlow<Boolean> = _authenticated.asStateFlow()

    private var officeBid: String = ""
    private var customerList: List<Customer> = emptyList()
    private var bookingList: List<Booking> = emptyList()

    override suspend fun clear() {
        officeBid = ""
        customerList = emptyList()
        bookingList = emptyList()
    }

    override fun isAuthenticatedFlow(): StateFlow<Boolean> =
        authenticated

    override fun setAuthenticated(value: Boolean) {
        _authenticated.value = value
    }

    override suspend fun setFakeAuthenticationExpire(value: Boolean) {
        if (!value)
            return

        // Simulating the expiring of a token
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                timer.cancel()
                setAuthenticated(false)
            }
        }, 10000, 10000)
    }

    override suspend fun setOfficeBid(value: String) {
        officeBid = value
    }

    override suspend fun getOfficeBid(): String =
        officeBid

    override suspend fun setCustomerList(value: List<Customer>) {
        customerList = value
    }

    override suspend fun getCustomerList(): List<Customer> =
        customerList

    override suspend fun setBookingList(value: List<Booking>) {
        bookingList = value
    }

    override suspend fun getBookingList(): List<Booking> =
        bookingList
}