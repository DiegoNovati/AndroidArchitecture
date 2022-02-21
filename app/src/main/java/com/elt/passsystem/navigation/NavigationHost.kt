package com.elt.passsystem.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elt.passsystem.screens.home.HomeScreen
import com.elt.passsystem.screens.login.LoginScreen
import com.elt.passsystem.services.IServiceNavigation
import com.elt.passsystem.services.Route

@Composable
fun NavigationHost(
    navController: NavHostController,
    initialDestination: Route,
    serviceNavigation: IServiceNavigation,
) {
    NavHost(
        navController = navController,
        startDestination = initialDestination.routeName,
    ) {
        composable(route = Route.Login.routeName) { LoginScreen(serviceNavigation = serviceNavigation) }
        composable(route = Route.Home.routeName) { HomeScreen(serviceNavigation = serviceNavigation) }
    }
}