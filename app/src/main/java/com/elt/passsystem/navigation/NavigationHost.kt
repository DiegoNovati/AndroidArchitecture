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