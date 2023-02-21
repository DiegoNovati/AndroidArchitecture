package uk.co.itmms.androidArchitecture.domain.repositories

import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import kotlinx.coroutines.flow.StateFlow

interface IRepositoryRuntime {

    suspend fun clear()

    fun isAuthenticatedFlow(): StateFlow<Boolean>
    fun setAuthenticated(value: Boolean)

    suspend fun setFakeAuthenticationExpire(value: Boolean)

    suspend fun setOfficeBid(value: String)
    suspend fun getOfficeBid(): String

    suspend fun setCustomerList(value: List<Customer>)
    suspend fun getCustomerList(): List<Customer>

    suspend fun setBookingList(value: List<Booking>)
    suspend fun getBookingList(): List<Booking>
}