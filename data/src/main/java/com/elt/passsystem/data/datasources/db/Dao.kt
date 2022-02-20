package com.elt.passsystem.data.datasources.db

import androidx.room.*
import com.elt.passsystem.data.models.DBBooking
import com.elt.passsystem.data.models.DBCustomer
import com.elt.passsystem.data.models.TableBookings
import com.elt.passsystem.data.models.TableCustomers

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
    @Query("SELECT count(*) from ${TableCustomers.tableName}")
    suspend fun count(): Int

    @Query("SELECT * FROM ${TableCustomers.tableName}")
    suspend fun list(): List<DBCustomer>

    @Query("SELECT * FROM ${TableCustomers.tableName} WHERE bid = :bid")
    suspend fun getByBid(bid: String): DBCustomer?
}

@Dao
interface DaoBookings: DaoCrud<DBBooking> {
    @Query("SELECT count(*) from ${TableBookings.tableName}")
    suspend fun count(): Int

    @Query("SELECT * FROM ${TableBookings.tableName}")
    suspend fun list(): List<DBBooking>

    @Query("SELECT * FROM ${TableBookings.tableName} WHERE id = :id")
    suspend fun getById(id: Long): DBBooking?
}