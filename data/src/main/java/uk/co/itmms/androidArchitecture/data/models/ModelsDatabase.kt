package uk.co.itmms.androidArchitecture.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

object TableCustomers {
    const val TABLE_NAME = "customers"
    const val FIELD_BID = "bid"
    const val FIELD_UUID = "uuid"
    const val FIELD_TITLE = "title"
    const val FIELD_FIRST_NAME = "firstname"
    const val FIELD_LOCATION = "location"
}

@Entity(
    tableName = TableCustomers.TABLE_NAME,
)
data class DBCustomer(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = TableCustomers.FIELD_BID)
    val bid: String,
    @ColumnInfo(name = TableCustomers.FIELD_UUID)
    val uuid: String,
    @ColumnInfo(name = TableCustomers.FIELD_TITLE)
    val title: String?,
    @ColumnInfo(name = TableCustomers.FIELD_FIRST_NAME)
    val firstname: String?,
    @ColumnInfo(name = TableCustomers.FIELD_LOCATION)
    val location: String?,
) {
    val name: String
        get() =
            title?.let { title ->
                firstname?.let { firstname ->
                    "$title $firstname"
                } ?: run {
                    title
                }
            } ?: run {
                firstname ?: ""
            }
}

enum class DBBookingStatus {
    Completed, Started, Scheduled, Unknown,
}

object TableBookings {
    const val TABLE_NAME = "bookings"
    const val FIELD_CUSTOMER_BID = "customerBid"
    const val FIELD_STATUS = "status"
    const val FIELD_START = "start"
    const val FIELD_END = "end"
}

@Entity(
    tableName = TableBookings.TABLE_NAME,
)
data class DBBooking(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo(name = TableBookings.FIELD_CUSTOMER_BID)
    val customerBid: String,
    @ColumnInfo(name = TableBookings.FIELD_STATUS)
    val status: DBBookingStatus,
    @ColumnInfo(name = TableBookings.FIELD_START)
    val start: Date,
    @ColumnInfo(name = TableBookings.FIELD_END)
    val end: Date,
)


