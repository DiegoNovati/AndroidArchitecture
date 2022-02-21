package com.elt.passsystem.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.elt.passsystem.services.IServiceNavigation

@Composable
fun HomeScreen(
    serviceNavigation: IServiceNavigation,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Hello, I am the HOME Screen",
            fontSize = 22.sp,
        )
        Button(onClick = {
            serviceNavigation.popBack()
        }) {
            Text(text = "Back")
        }
    }
}