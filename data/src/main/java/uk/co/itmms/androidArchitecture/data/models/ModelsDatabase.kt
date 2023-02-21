package uk.co.itmms.androidArchitecture.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

object TableCustomers {
    const val tableName = "customers"
    const val fieldBid = "bid"
    const val fieldUuid = "uuid"
    const val fieldTitle = "title"
    const val fieldFirstName = "firstname"
    const val fieldLocation = "location"
}

@Entity(
    tableName = TableCustomers.tableName,
)
data class DBCustomer(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = TableCustomers.fieldBid)
    val bid: String,
    @ColumnInfo(name = TableCustomers.fieldUuid)
    val uuid: String,
    @ColumnInfo(name = TableCustomers.fieldTitle)
    val title: String?,
    @ColumnInfo(name = TableCustomers.fieldFirstName)
    val firstname: String?,
    @ColumnInfo(name = TableCustomers.fieldLocation)
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
    const val tableName = "bookings"
    const val fieldCustomerBid = "customerBid"
    const val fieldStatus = "status"
    const val fieldStart = "start"
    const val fieldEnd = "end"
}

@Entity(
    tableName = TableBookings.tableName,
)
data class DBBooking(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo(name = TableBookings.fieldCustomerBid)
    val customerBid: String,
    @ColumnInfo(name = TableBookings.fieldStatus)
    val status: DBBookingStatus,
    @ColumnInfo(name = TableBookings.fieldStart)
    val start: Date,
    @ColumnInfo(name = TableBookings.fieldEnd)
    val end: Date,
)


