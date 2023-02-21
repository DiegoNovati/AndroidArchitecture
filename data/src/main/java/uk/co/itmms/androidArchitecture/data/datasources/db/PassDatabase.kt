package uk.co.itmms.androidArchitecture.data.datasources.db

import android.content.Context
import androidx.room.*
import uk.co.itmms.androidArchitecture.data.models.DBBooking
import uk.co.itmms.androidArchitecture.data.models.DBCustomer
import java.text.SimpleDateFormat
import java.util.*

interface IPassDatabase {
    fun daoCustomers(): DaoCustomers
    fun daoBookings(): DaoBookings
}

// To support auto-migration:
//    https://medium.com/androiddevelopers/room-auto-migrations-d5370b0ca6eb
@Database(
    version = 1,
    entities = [
        DBCustomer::class,
        DBBooking::class,
    ],
    autoMigrations = [
//        AutoMigration(from = 1, to = 2),
    ],
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
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

class RoomConverters {

    @TypeConverter
    fun dateToString(date: Date): String = roomDateFormat.format(date)

    @TypeConverter
    fun stringToDate(value: String): Date =
        roomDateFormat.parse(value) ?: Date()
}

val roomDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK)