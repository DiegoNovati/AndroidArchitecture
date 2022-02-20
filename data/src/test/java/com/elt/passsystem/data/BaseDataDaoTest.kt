package com.elt.passsystem.data

import androidx.room.Room
import com.elt.passsystem.data.datasources.db.DaoBookings
import com.elt.passsystem.data.datasources.db.DaoCustomers
import com.elt.passsystem.data.datasources.db.PassDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

abstract class BaseDataDaoTest: BaseDataRobolectricTest() {

    lateinit var daoCustomers: DaoCustomers
    lateinit var daoBookings: DaoBookings

    private lateinit var passDatabase: PassDatabase

    @Before
    fun createDatabase() = runBlocking(Dispatchers.IO) {
        passDatabase = Room
            .inMemoryDatabaseBuilder(context, PassDatabase::class.java)
            .build()
        daoCustomers = passDatabase.daoCustomers()
        daoBookings = passDatabase.daoBookings()
    }

    @After
    fun destroyDatabase() = runBlocking(Dispatchers.IO) {
        if (this@BaseDataDaoTest::passDatabase.isInitialized) {
            passDatabase.clearAllTables()
            passDatabase.close()
        }
    }
}