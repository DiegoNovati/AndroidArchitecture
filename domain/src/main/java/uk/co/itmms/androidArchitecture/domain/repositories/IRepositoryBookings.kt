package uk.co.itmms.androidArchitecture.domain.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.domain.entities.Booking

interface IRepositoryBookings {
    suspend fun getBookingList(officeBid: String): Either<RepositoryBackendFailure, List<Booking>>
}