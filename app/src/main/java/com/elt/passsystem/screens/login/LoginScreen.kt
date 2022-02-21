package com.elt.passsystem.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.elt.passsystem.services.IServiceNavigation
import com.elt.passsystem.services.Route

@Composable
fun LoginScreen(
    serviceNavigation: IServiceNavigation,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Hello, I am the Login Screen",
            fontSize = 22.sp,
        )
        Button(onClick = {
            serviceNavigation.open(Route.Home)
        }) {
            Text(text = "Open Home")
        }
    }
}