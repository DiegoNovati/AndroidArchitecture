package uk.co.itmms.androidArchitecture.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.co.itmms.androidArchitecture.navigationGraph.NavigationHost
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            viewModel.setNavController(
                navController = navController,
            )

            AndroidArchitectureTheme {
                BackHandler {
                    viewModel.backButtonPressed()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationHost(
                        navController = navController,
                        serviceNavigation = viewModel.getServiceNavigation(),
                    )
                }
            }
        }
    }
}