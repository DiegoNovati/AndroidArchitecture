package uk.co.itmms.androidArchitecture.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.components.AppDivider
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
import uk.co.itmms.androidArchitecture.extensions.toDescription
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeBookingRow(
    booking: Booking,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        HomeBooking(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            booking = booking,
        )
        if (showDivider) {
            AppDivider()
        }
    }
}

@Composable
private fun HomeBooking(
    booking: Booking,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
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
}

private val formatTime = SimpleDateFormat("HH:mm", Locale.ENGLISH)

private val booking = Booking(
    3,
    "1",
    BookingStatus.Completed,
    formatTime.parse("18:00")!!,
    formatTime.parse("20:00")!!,
)

@PreviewAppScreen
@Composable
private fun HomeBookingRowWithDividerPreview() {
    AndroidArchitectureTheme {
        HomeBookingRow(
            booking = booking,
            showDivider = true,
        )
    }
}

@PreviewAppScreen
@Composable
private fun HomeBookingRowWithoutDividerPreview() {
    AndroidArchitectureTheme {
        HomeBookingRow(
            booking = booking,
            showDivider = false,
        )
    }
}

@PreviewAppScreen
@Composable
private fun HomeBookingPreview() {
    AndroidArchitectureTheme {
        HomeBooking(
            booking = booking,
        )
    }
}