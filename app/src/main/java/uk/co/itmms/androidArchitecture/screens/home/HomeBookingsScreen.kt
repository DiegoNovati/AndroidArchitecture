package uk.co.itmms.androidArchitecture.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
import uk.co.itmms.androidArchitecture.extensions.toDescription
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeBookingsScreen(
    bookingList: List<Booking>,
) {
    val formatTime = SimpleDateFormat("HH:mm", Locale.getDefault())
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .padding(16.dp),
        state = listState,
    ) {
        itemsIndexed(bookingList) { index, booking ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${formatTime.format(booking.start)} - ${formatTime.format(booking.end)}",
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    Text(
                        text = booking.status.toDescription(),
                        style = MaterialTheme.typography.body1
                    )
                }
                if (index < bookingList.lastIndex)
                    Divider(
                        color = Color.Gray.copy(alpha = 0.4f),
                        thickness = 1.dp,
                    )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeBookingsScreenPreview() {
    val formatTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val bookingList = listOf(
        Booking(
            1,
            "1",
            BookingStatus.Scheduled,
            formatTime.parse("08:00:00")!!,
            formatTime.parse("12:00:00")!!,
        ),
        Booking(
            2,
            "1",
            BookingStatus.Started,
            formatTime.parse("14:00:00")!!,
            formatTime.parse("16:00:00")!!,
        ),
        Booking(
            3,
            "1",
            BookingStatus.Completed,
            formatTime.parse("18:00:00")!!,
            formatTime.parse("20:00:00")!!,
        ),
    )
    AndroidArchitectureTheme {
        HomeBookingsScreen(
            bookingList = bookingList,
        )
    }
}