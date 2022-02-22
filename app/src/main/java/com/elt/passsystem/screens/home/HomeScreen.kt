package com.elt.passsystem.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elt.passsystem.R
import com.elt.passsystem.domain.entities.Customer
import com.elt.passsystem.domain.entities.Booking
import com.elt.passsystem.domain.entities.BookingStatus
import com.elt.passsystem.domain.entities.LoginResult
import com.elt.passsystem.extensions.toDescription
import com.elt.passsystem.ui.theme.AndroidArchitectureTheme
import com.elt.passsystem.widgets.ButtonRoundedEdgesPrimary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val loginResult = viewModel.state.observeAsState(
        LoginResult("", listOf(), listOf())
    ).value

    HomeScreenUI(
        customerList = loginResult.customerList,
        bookingList = loginResult.bookingList,
        onLogout = {
            viewModel.logout()
        }
    )
}

@Composable
fun HomeScreenUI(
    customerList: List<Customer>,
    bookingList: List<Booking>,
    onLogout: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        item {
            TextTitle(
                text = "Customers"
            )
        }
        items(customerList) { customer ->
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "${customer.name} - ${customer.address}"
            )
        }
        item {
            TextTitle(text = "Bookings")
        }
        items(bookingList) { booking ->
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "${booking.bookingBid} - ${booking.status.toDescription()}"
            )
        }
        item {
            ButtonRoundedEdgesPrimary(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                stringId = R.string.homeLogoutButton,
                onClick = { onLogout() }
            )
        }
    }
}

@Composable
fun TextTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.body1.copy(
            fontWeight = FontWeight.Bold,
        ),
    )
}

@Composable
@Preview(showBackground = true)
fun HomeScreenUIPreview() {
    val customerList = listOf(
        Customer("1", "Mr. John", "25 Oxford Circus, London"),
        Customer("2", "Mrs. Jane", "4 Piccadilly Circus, London"),
    )
    val bookingList = listOf(
        Booking(1, "1", BookingStatus.Scheduled),
        Booking(2, "1", BookingStatus.Started),
        Booking(3, "1", BookingStatus.Completed),
    )
    AndroidArchitectureTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            HomeScreenUI(
                customerList = customerList,
                bookingList = bookingList,
                onLogout = {}
            )
        }
    }
}