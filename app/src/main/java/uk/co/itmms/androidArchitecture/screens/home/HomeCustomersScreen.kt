package uk.co.itmms.androidArchitecture.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

@Composable
fun HomeCustomersScreen(
    customerList: List<Customer>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(modifier),
        state = rememberLazyListState(),
    ) {
        itemsIndexed(customerList) { index, customer ->
            HomeCustomerRow(
                customer = customer,
                showDivider = index < customerList.lastIndex,
            )
        }
    }
}

@PreviewAppScreen
@Composable
private fun HomeCustomersScreenPreview() {
    val customerList = listOf(
        Customer("1", "Mr. John", "25 Oxford Circus, London"),
        Customer("2", "Mrs. Jane", "4 Piccadilly Circus, London"),
        Customer("3", "Mr. Paul", "12 Piccadilly Circus, London"),
    )
    AndroidArchitectureTheme {
        HomeCustomersScreen(
            customerList = customerList,
        )
    }
}