package com.elt.passsystem.domain.entities

data class LoginResult(
    val officeBid: String,
    val customerList: List<Customer>,
    val bookingList: List<Booking>,
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
    val bookingBid: String,
    val customerBid: String,
    val status: BookingStatus
)