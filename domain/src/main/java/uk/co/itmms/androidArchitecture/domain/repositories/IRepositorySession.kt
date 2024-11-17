package uk.co.itmms.androidArchitecture.domain.repositories

import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer

/**
 * This class should contain all the data when the user is authenticated
 */
interface IRepositorySession {
    suspend fun clear()

    var officeBid: String
    var customerList: List<Customer>
    var bookingList: List<Booking>
}