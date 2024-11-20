package uk.co.itmms.androidArchitecture.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.components.AppDivider
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

@Composable
fun HomeCustomerRow(
    customer: Customer,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        HomeCustomer(
            modifier = Modifier
                .padding(vertical = 16.dp),
            customer = customer,
        )
        if (showDivider) {
            AppDivider()
        }
    }
}

@Composable
private fun HomeCustomer(
    customer: Customer,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
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
}

@PreviewAppScreen
@Composable
private fun HomeCustomerRowWithDividerPreview() {
    AndroidArchitectureTheme {
        HomeCustomerRow(
            customer = Customer("1", "Mr. John", "25 Oxford Circus, London"),
            showDivider = true,
        )
    }
}

@PreviewAppScreen
@Composable
private fun HomeCustomerRowWithoutDividerPreview() {
    AndroidArchitectureTheme {
        HomeCustomerRow(
            customer = Customer("1", "Mr. John", "25 Oxford Circus, London"),
            showDivider = false,
        )
    }
}

@PreviewAppScreen
@Composable
private fun HomeCustomerPreview() {
    AndroidArchitectureTheme {
        HomeCustomer(
            customer = Customer("1", "Mr. John", "25 Oxford Circus, London"),
        )
    }
}