package uk.co.itmms.androidArchitecture.data

import androidx.room.Room
import uk.co.itmms.androidArchitecture.data.datasources.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import uk.co.itmms.androidArchitecture.data.datasources.db.DaoProducts
import uk.co.itmms.androidArchitecture.data.datasources.db.DaoTodos

abstract class BaseDataDaoTest: BaseDataRobolectricTest() {

    lateinit var daoProducts: DaoProducts
    lateinit var daoTodos: DaoTodos

    private lateinit var appDatabase: AppDatabase

    @Before
    fun createDatabase() = runBlocking(Dispatchers.IO) {
        appDatabase = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        daoProducts = appDatabase.daoProducts()
        daoTodos = appDatabase.daoTodos()
    }

    @After
    fun destroyDatabase() = runBlocking(Dispatchers.IO) {
        if (this@BaseDataDaoTest::appDatabase.isInitialized) {
            appDatabase.clearAllTables()
            appDatabase.close()
        }
    }
}