package uk.co.itmms.androidArchitecture.data.repositories

import arrow.core.Either
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceBackend
import uk.co.itmms.androidArchitecture.data.datasources.IDataSourceDatabaseBookings
import uk.co.itmms.androidArchitecture.data.extensions.toBookingList
import uk.co.itmms.androidArchitecture.data.extensions.toDBBookingList
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryBookings
import uk.co.itmms.androidArchitecture.domain.repositories.RepositoryBackendFailure

class RepositoryBookings(
    private val dataSourceBackend: IDataSourceBackend,
    private val dataSourceDatabaseBookings: IDataSourceDatabaseBookings,
) : IRepositoryBookings {

    override suspend fun getBookingList(
        officeBid: String
    ): Either<RepositoryBackendFailure, List<Booking>> =
        invokeRepository({
            val netBookingsResponse = dataSourceBackend.getBookingList(officeBid)
            val dbBookingList = netBookingsResponse.bookings.toDBBookingList()
            dataSourceDatabaseBookings.deleteAll()
            dataSourceDatabaseBookings.insert(dbBookingList)
            Either.Right(dbBookingList.toBookingList())
        }){
            Either.Right(dataSourceDatabaseBookings.list().toBookingList())
        }
}