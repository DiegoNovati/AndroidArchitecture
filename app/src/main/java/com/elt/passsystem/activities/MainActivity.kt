package com.elt.passsystem.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.elt.passsystem.navigation.NavigationHost
import com.elt.passsystem.services.Route
import com.elt.passsystem.services.ServiceNavigation
import com.elt.passsystem.ui.theme.AndroidArchitectureTheme

class MainActivity : ComponentActivity() {
    private val serviceNavigation = ServiceNavigation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            serviceNavigation.setNavController(
                navController = navController,
                initialRoute = Route.Login,
            )

            AndroidArchitectureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationHost(
                        navController = navController,
                        initialDestination = serviceNavigation.getInitialRoute(),
                        serviceNavigation = serviceNavigation,
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        serviceNavigation.updateCurrentRoute()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidArchitectureTheme {
        Greeting("Android")
    }
}