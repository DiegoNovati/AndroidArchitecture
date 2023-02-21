package uk.co.itmms.androidArchitecture.data.models

import java.util.*

data class NetAuthenticateResponse(
    val token: String,
    val offices: List<NetAuthenticateOffice>,
)

data class NetAuthenticateOffice(
    val bid: String,
    val display: String,
    val v2: Boolean,
    val cdn: NetAuthenticateOfficeCdn,
)

data class NetAuthenticateOfficeCdn(
    val baseUrl: String,
    val policy: String,
    val signature: String,
    val keyPairId: String,
)

data class NetCustomersResponse(
    val customers: List<NetCustomer>,
)

data class NetCustomer(
    val id: Long,
    val bid: String,
    val uuid: String,
    val title: String,
    val firstname: String,
    val nickname: String,
    val surname: String,
    val location: String,
    val status: String,
    val dnr: Boolean,
    val dols: Boolean,
    val dob: String,
    val allergies: Boolean,
    val modified: Date,
    val photoKey: String?,
    val careplanReviewDate: String,
)

data class NetBookingsResponse(
    val start: Date,
    val end: Date,
    val bookings: List<NetBooking>
)

data class NetBooking(
    val id: Long,
    val customerBid: String,
    val status: String,
    val nfcTag: Int,
    val otherCareworkers: List<NetNetBookingCareWorker>,
    val start: Date,
    val end: Date,
)

data class NetNetBookingCareWorker(
    val bid: String,
    val name: String?,
    val phone: String?,
)