package com.elt.passsystem.data.datasources.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elt.passsystem.data.models.DBBooking
import com.elt.passsystem.data.models.DBCustomer

interface IPassDatabase {
    fun daoCustomers(): DaoCustomers
    fun daoBookings(): DaoBookings
}

@Database(
    version = 1,
    entities = [
        DBCustomer::class,
        DBBooking::class,
    ]
)
abstract class PassDatabase : IPassDatabase, RoomDatabase() {
    abstract override fun daoCustomers(): DaoCustomers
    abstract override fun daoBookings(): DaoBookings
}

fun openDatabase(applicationContext: Context): PassDatabase =
    Room
        .databaseBuilder(
            applicationContext,
            PassDatabase::class.java,
            "PassDatabase.db"
        )
        .build()