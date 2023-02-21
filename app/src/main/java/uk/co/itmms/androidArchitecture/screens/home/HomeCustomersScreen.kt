package uk.co.itmms.androidArchitecture.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

@Composable
fun HomeCustomersScreen(
    customerList: List<Customer>,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .padding(16.dp),
        state = listState,
    ) {
        itemsIndexed(customerList) { index, customer ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                ) {
                    Text(
                        text = customer.name,
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    Text(
                        text = customer.address,
                        style = MaterialTheme.typography.body1
                    )
                }
                if (index < customerList.lastIndex)
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
fun HomeCustomersScreenPreview() {
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