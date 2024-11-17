package uk.co.itmms.androidArchitecture.data

import androidx.room.Room
import uk.co.itmms.androidArchitecture.data.datasources.db.DaoBookings
import uk.co.itmms.androidArchitecture.data.datasources.db.DaoCustomers
import uk.co.itmms.androidArchitecture.data.datasources.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

abstract class BaseDataDaoTest: BaseDataRobolectricTest() {

    lateinit var daoCustomers: DaoCustomers
    lateinit var daoBookings: DaoBookings

    private lateinit var passDatabase: AppDatabase

    @Before
    fun createDatabase() = runBlocking(Dispatchers.IO) {
        passDatabase = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
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