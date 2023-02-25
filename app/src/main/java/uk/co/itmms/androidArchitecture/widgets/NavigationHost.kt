package uk.co.itmms.androidArchitecture.widgets

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.co.itmms.androidArchitecture.screens.home.HomeScreen
import uk.co.itmms.androidArchitecture.screens.login.LoginScreen
import uk.co.itmms.androidArchitecture.services.IServiceNavigation
import uk.co.itmms.androidArchitecture.services.Route

@Composable
fun NavigationHost(
    navController: NavHostController,
    serviceNavigation: IServiceNavigation,
) {
    NavHost(
        navController = navController,
        startDestination = serviceNavigation.getInitialRoute().routeName,
    ) {
        composable(route = Route.Login.routeName) { LoginScreen() }
        composable(route = Route.Home.routeName) { HomeScreen() }
    }
}