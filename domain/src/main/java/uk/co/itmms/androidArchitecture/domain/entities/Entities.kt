package uk.co.itmms.androidArchitecture.domain.entities

import java.util.Date

data class Customer(
    val customerBid: String,
    val name: String,
    val address: String,
)

sealed class BookingStatus {
    object Scheduled: BookingStatus()
    object Started: BookingStatus()
    object Completed: BookingStatus()
}

data class Booking(
    val bookingBid: Long,
    val customerBid: String,
    val status: BookingStatus,
    val start: Date,
    val end: Date,
)