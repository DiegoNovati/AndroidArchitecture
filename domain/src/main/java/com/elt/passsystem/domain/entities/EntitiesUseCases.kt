package com.elt.passsystem.domain.entities

data class LoginResult(
    val officeBid: String,
    val customerList: List<Customer> = listOf(),
    val bookingList: List<Booking> = listOf(),
)

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
    val status: BookingStatus
)