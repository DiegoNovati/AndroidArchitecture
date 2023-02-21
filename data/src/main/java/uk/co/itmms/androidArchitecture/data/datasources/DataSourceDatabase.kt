package uk.co.itmms.androidArchitecture.data.datasources

import uk.co.itmms.androidArchitecture.data.datasources.db.IPassDatabase
import uk.co.itmms.androidArchitecture.data.models.DBBooking
import uk.co.itmms.androidArchitecture.data.models.DBBookingStatus
import uk.co.itmms.androidArchitecture.data.models.DBCustomer

interface IDataSourceDatabaseCustomers {
    suspend fun list(): List<DBCustomer>
    suspend fun listByTitle(title: String): List<DBCustomer>
    suspend fun insert(customer: DBCustomer)
    suspend fun insert(list: List<DBCustomer>)
    suspend fun update(customer: DBCustomer)
    suspend fun update(list: List<DBCustomer>)
    suspend fun delete(customer: DBCustomer)
    suspend fun deleteAll()
}

interface IDataSourceDatabaseBookings {
    suspend fun list(): List<DBBooking>
    suspend fun listByStatus(status: DBBookingStatus): List<DBBooking>
    suspend fun insert(booking: DBBooking)
    suspend fun insert(list: List<DBBooking>)
    suspend fun update(booking: DBBooking)
    suspend fun update(list: List<DBBooking>)
    suspend fun delete(booking: DBBooking)
    suspend fun deleteAll()
}

class DataSourceDatabaseCustomers(
    private val passDatabase: IPassDatabase,
): IDataSourceDatabaseCustomers {

    override suspend fun list(): List<DBCustomer> =
        passDatabase.daoCustomers()
            .list()

    override suspend fun listByTitle(title: String): List<DBCustomer> =
        passDatabase.daoCustomers()
            .list()
            .filter { it.title == title }

    override suspend fun insert(customer: DBCustomer) =
        passDatabase.daoCustomers()
            .insert(customer)

    override suspend fun insert(list: List<DBCustomer>) =
        passDatabase.daoCustomers()
            .insert(list)

    override suspend fun update(customer: DBCustomer) =
        passDatabase.daoCustomers()
            .update(customer)

    override suspend fun update(list: List<DBCustomer>) =
        list.forEach {
            passDatabase.daoCustomers()
                .update(it)
        }

    override suspend fun delete(customer: DBCustomer) =
        passDatabase.daoCustomers()
            .delete(customer)

    override suspend fun deleteAll() =
        passDatabase.daoCustomers()
            .list()
            .forEach {
                passDatabase.daoCustomers()
                    .delete(it)
            }
}

class DataSourceDatabaseBookings(
    private val passDatabase: IPassDatabase,
): IDataSourceDatabaseBookings {

    override suspend fun list(): List<DBBooking> =
        passDatabase.daoBookings()
            .list()

    override suspend fun listByStatus(status: DBBookingStatus): List<DBBooking> =
        passDatabase.daoBookings()
            .list()
            .filter { it.status == status }

    override suspend fun insert(booking: DBBooking) =
        passDatabase.daoBookings()
            .insert(booking)

    override suspend fun insert(list: List<DBBooking>) =
        passDatabase.daoBookings()
            .insert(list)

    override suspend fun update(booking: DBBooking) =
        passDatabase.daoBookings()
            .update(booking)

    override suspend fun update(list: List<DBBooking>) =
        list.forEach {
            passDatabase.daoBookings()
                .update(it)
        }

    override suspend fun delete(booking: DBBooking) =
        passDatabase.daoBookings()
            .delete(booking)

    override suspend fun deleteAll() =
        passDatabase.daoBookings()
            .list()
            .forEach {
                passDatabase.daoBookings()
                    .delete(it)
            }
}