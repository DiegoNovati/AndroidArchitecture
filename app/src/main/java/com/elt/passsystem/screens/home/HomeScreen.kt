package com.elt.passsystem.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elt.passsystem.R
import com.elt.passsystem.ui.theme.AndroidArchitectureTheme
import com.elt.passsystem.widgets.ButtonRoundedEdgesPrimary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeScreenUI(
        onLogout = {
            viewModel.logout()
        }
    )
}

@Composable
fun HomeScreenUI(
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
    ) {
        ButtonRoundedEdgesPrimary(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            stringId = R.string.homeLogoutButton,
            onClick = { onLogout() }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenUIPreview() {
    AndroidArchitectureTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            HomeScreenUI(
                onLogout = {}
            )
        }
    }
}