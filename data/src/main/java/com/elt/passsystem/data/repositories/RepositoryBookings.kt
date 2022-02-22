package com.elt.passsystem.data.repositories

import arrow.core.Either
import com.elt.passsystem.data.datasources.IDataSourceBackend
import com.elt.passsystem.data.datasources.IDataSourceDatabaseBookings
import com.elt.passsystem.data.extensions.toBookingList
import com.elt.passsystem.data.extensions.toDBBookingList
import com.elt.passsystem.domain.entities.Booking
import com.elt.passsystem.domain.repositories.IRepositoryBookings
import com.elt.passsystem.domain.repositories.RepositoryBackendFailure

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