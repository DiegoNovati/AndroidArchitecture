package com.elt.passsystem.data.extensions

import com.elt.passsystem.data.models.DBBooking
import com.elt.passsystem.data.models.DBBookingStatus
import com.elt.passsystem.data.models.NetBooking
import com.elt.passsystem.domain.entities.Booking
import com.elt.passsystem.domain.entities.BookingStatus
import java.util.*

fun DBBooking.toBooking(): Booking =
    Booking(
        bookingBid = this.id,
        customerBid = this.customerBid,
        status = this.status.toBookingStatus(),
        start = this.start,
        end = this.end,
    )

/**
 * Exporting customer entities excluding those having the status Unknown
 */
fun List<DBBooking>.toBookingList(): List<Booking> =
    this
        .filter { it.status != DBBookingStatus.Unknown }
        .map { it.toBooking() }

fun NetBooking.toDBBooking(): DBBooking =
    DBBooking(
        id = this.id,
        customerBid = this.customerBid,
        status = netStatusToBookingStatus(this.status),
        start = this.start,
        end = this.end,
    )

fun List<NetBooking>.toDBBookingList(): List<DBBooking> =
    this.map { it.toDBBooking() }

private fun netStatusToBookingStatus(status: String): DBBookingStatus =
    when (status) {
        "SCHEDULED" -> DBBookingStatus.Scheduled
        "STARTED" -> DBBookingStatus.Started
        "COMPLETED" -> DBBookingStatus.Completed
        "UNKNOWN" -> DBBookingStatus.Unknown
        else -> DBBookingStatus.Unknown
    }

private fun DBBookingStatus.toBookingStatus(): BookingStatus =
    when (this) {
        DBBookingStatus.Completed -> BookingStatus.Completed
        DBBookingStatus.Started -> BookingStatus.Started
        DBBookingStatus.Scheduled -> BookingStatus.Scheduled
        DBBookingStatus.Unknown -> throw InvalidPropertiesFormatException("Trying to convert the DBCustomer status Unknown")
    }