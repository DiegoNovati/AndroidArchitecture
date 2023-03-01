package uk.co.itmms.androidArchitecture.data.datasources.db

import android.content.Context
import androidx.room.*
import uk.co.itmms.androidArchitecture.data.models.DBBooking
import uk.co.itmms.androidArchitecture.data.models.DBCustomer
import uk.co.itmms.androidArchitecture.data.models.DBProduct
import uk.co.itmms.androidArchitecture.data.models.DBTodo
import java.text.SimpleDateFormat
import java.util.*

interface IAppDatabase {
    fun daoProducts(): DaoProducts
    fun daoTodos(): DaoTodos
}

// To support auto-migration:
//    https://medium.com/androiddevelopers/room-auto-migrations-d5370b0ca6eb
@Database(
    version = 1,
    entities = [
        DBProduct::class,
        DBTodo::class,
    ],
    autoMigrations = [
//        AutoMigration(from = 1, to = 2),
    ],
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : IAppDatabase, RoomDatabase() {
    abstract override fun daoProducts(): DaoProducts
    abstract override fun daoTodos(): DaoTodos
}

fun openDatabase(applicationContext: Context): AppDatabase =
    Room
        .databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "AppDatabase.db"
        )
        .build()

class RoomConverters {

    @TypeConverter
    fun dateToString(date: Date): String = roomDateFormat.format(date)

    @TypeConverter
    fun stringToDate(value: String): Date =
        roomDateFormat.parse(value) ?: Date()
}

val roomDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK)