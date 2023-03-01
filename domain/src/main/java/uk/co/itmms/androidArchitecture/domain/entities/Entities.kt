package uk.co.itmms.androidArchitecture.domain.entities

import java.util.Date

enum class Gender {
    Male, Female,
}

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: Gender,
    val image: String,
)

data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: Long,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Long,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
)

data class Todo(
    val id: Long,
    val todo: String,
    val completed: Boolean,
)

// ***********************************************
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