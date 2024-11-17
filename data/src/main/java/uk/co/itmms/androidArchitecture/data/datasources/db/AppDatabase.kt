package uk.co.itmms.androidArchitecture.data.datasources.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import uk.co.itmms.androidArchitecture.data.models.DBBooking
import uk.co.itmms.androidArchitecture.data.models.DBCustomer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface IAppDatabase {
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
abstract class AppDatabase : IAppDatabase, RoomDatabase() {
    abstract override fun daoCustomers(): DaoCustomers
    abstract override fun daoBookings(): DaoBookings
}

fun openDatabase(applicationContext: Context): AppDatabase =
    Room
        .databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
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