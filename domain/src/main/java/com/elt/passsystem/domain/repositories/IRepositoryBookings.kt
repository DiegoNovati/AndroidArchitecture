package com.elt.passsystem.domain.repositories

import arrow.core.Either
import com.elt.passsystem.domain.entities.Booking

interface IRepositoryBookings {

    suspend fun getBookingList(officeBid: String): Either<RepositoryBackendFailure, List<Booking>>
}