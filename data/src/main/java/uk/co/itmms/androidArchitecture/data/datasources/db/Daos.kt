package uk.co.itmms.androidArchitecture.data.datasources.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.co.itmms.androidArchitecture.data.models.DBBooking
import uk.co.itmms.androidArchitecture.data.models.DBCustomer
import uk.co.itmms.androidArchitecture.data.models.TableBookings
import uk.co.itmms.androidArchitecture.data.models.TableCustomers

@Dao
interface DaoCrud<in Type> {
    @Insert
    suspend fun insert(type: Type)

    @Insert
    suspend fun insert(list: List<Type>)

    @Update
    suspend fun update(type: Type)

    @Delete
    suspend fun delete(type: Type)

    @Delete
    suspend fun delete(list: List<Type>)
}

@Dao
interface DaoCustomers: DaoCrud<DBCustomer> {
    @Query("SELECT count(*) from ${TableCustomers.TABLE_NAME}")
    suspend fun count(): Int

    @Query("SELECT * FROM ${TableCustomers.TABLE_NAME}")
    suspend fun list(): List<DBCustomer>

    @Query("SELECT * FROM ${TableCustomers.TABLE_NAME} WHERE bid = :bid")
    suspend fun getByBid(bid: String): DBCustomer?
}

@Dao
interface DaoBookings: DaoCrud<DBBooking> {
    @Query("SELECT count(*) from ${TableBookings.TABLE_NAME}")
    suspend fun count(): Int

    @Query("SELECT * FROM ${TableBookings.TABLE_NAME}")
    suspend fun list(): List<DBBooking>

    @Query("SELECT * FROM ${TableBookings.TABLE_NAME} WHERE id = :id")
    suspend fun getById(id: Long): DBBooking?
}