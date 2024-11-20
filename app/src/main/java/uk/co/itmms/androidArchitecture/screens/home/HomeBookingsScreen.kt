package uk.co.itmms.androidArchitecture.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeBookingsScreen(
    bookingList: List<Booking>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(modifier),
        state = rememberLazyListState(),
    ) {
        itemsIndexed(bookingList) { index, booking ->
            HomeBookingRow(
                modifier = Modifier
                    .fillMaxWidth(),
                booking = booking,
                showDivider = index < bookingList.lastIndex,
            )
        }
    }
}

@PreviewAppScreen
@Composable
private fun HomeBookingsScreenPreview() {
    AndroidArchitectureTheme {
        HomeBookingsScreen(
            bookingList = getBookingList(),
        )
    }
}

private fun getBookingList(): List<Booking> {
    val formatTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return listOf(
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
}