package com.elt.passsystem.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.elt.passsystem.R
import com.elt.passsystem.domain.entities.Booking
import com.elt.passsystem.domain.entities.BookingStatus
import com.elt.passsystem.domain.entities.Customer
import com.elt.passsystem.ui.theme.AndroidArchitectureTheme
import java.text.SimpleDateFormat
import java.util.*

sealed class BottomBarRoute(val routeName: String, val isInitialRoute: Boolean = false) {
    object Bookings : BottomBarRoute("Bookings")
    object Customers : BottomBarRoute("Customers", true)

    companion object {
        fun getInitialRoute(): BottomBarRoute =
            BottomBarRoute::class.sealedSubclasses
                .firstOrNull { it.objectInstance?.isInitialRoute == true }
                ?.objectInstance
                ?: Bookings
    }
}

sealed class BottomBarScreen(
    val route: BottomBarRoute,
    @StringRes val titleId: Int,
    val icon: ImageVector,
) {
    object Bookings : BottomBarScreen(
        route = BottomBarRoute.Bookings,
        titleId = R.string.homeBottomScreenBookingsTitle,
        icon = Icons.Filled.DateRange,
    )

    object Customers : BottomBarScreen(
        route = BottomBarRoute.Customers,
        titleId = R.string.homeBottomScreenCustomersTitle,
        icon = Icons.Filled.Person,
    )
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    val connected: Boolean
    val customerList: List<Customer>
    val bookingList: List<Booking>
    viewModel.state.observeAsState(HomeViewModel.State()).value.apply {
        connected = this.connected
        customerList = this.data.customerList
        bookingList = this.data.bookingList
    }

    HomeScreenUI(
        navController = navController,
        customerList = customerList,
        bookingList = bookingList,
        connected = connected,
        onLogout = { viewModel.logout() }
    )
}

@Composable
fun HomeScreenUI(
    navController: NavHostController,
    customerList: List<Customer>,
    bookingList: List<Booking>,
    connected: Boolean,
    onLogout: () -> Unit,
) {
    val connectedText = if (!connected) stringResource(id = R.string.connectionMissing) else ""

    Scaffold(
        topBar = {
            HomeTopBar(
                connectedText = connectedText,
                onLogout = onLogout,
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
            )
        }
    ) {
        BottomNavigationHost(
            navController = navController,
            customerList = customerList,
            bookingList = bookingList,
        )
    }
}

@Composable
fun HomeTopBar(
    connectedText: String,
    onLogout: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.homeTitle, connectedText),
            )
        },
        actions = {
            IconButton(
                onClick = {
                    onLogout()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = stringResource(id = R.string.homeLogoutButton),
                )
            }
        }
    )
}

@Composable
fun BottomBar(
    navController: NavHostController,
) {
    val screens = listOf(
        BottomBarScreen.Bookings,
        BottomBarScreen.Customers,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController,
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    BottomNavigationItem(
        label = {
            Text(
                text = stringResource(id = screen.titleId)
            )
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = stringResource(id = screen.titleId),
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route.routeName
        } == true,
        onClick = {
            navController.navigate(screen.route.routeName) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

@Composable
fun BottomNavigationHost(
    navController: NavHostController,
    customerList: List<Customer>,
    bookingList: List<Booking>,
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarRoute.getInitialRoute().routeName,
    ) {
        composable(route = BottomBarRoute.Bookings.routeName) { HomeBookingsScreen(bookingList) }
        composable(route = BottomBarRoute.Customers.routeName) { HomeCustomersScreen(customerList) }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenUIPreview() {
    val navController = rememberNavController()
    val customerList = listOf(
        Customer("1", "Mr. John", "25 Oxford Circus, London"),
        Customer("2", "Mrs. Jane", "4 Piccadilly Circus, London"),
    )
    val formatTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val bookingList = listOf(
        Booking(1, "1", BookingStatus.Scheduled, formatTime.parse("08:00:00")!!, formatTime.parse("12:00:00")!!,),
        Booking(2, "1", BookingStatus.Started, formatTime.parse("14:00:00")!!, formatTime.parse("16:00:00")!!,),
        Booking(3, "1", BookingStatus.Completed, formatTime.parse("18:00:00")!!, formatTime.parse("20:00:00")!!,),
    )
    AndroidArchitectureTheme {
        HomeScreenUI(
            navController = navController,
            customerList = customerList,
            bookingList = bookingList,
            connected = true,
            onLogout = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenUIDisconnectedPreview() {
    val navController = rememberNavController()
    val customerList = listOf(
        Customer("1", "Mr. John", "25 Oxford Circus, London"),
        Customer("2", "Mrs. Jane", "4 Piccadilly Circus, London"),
    )
    val formatTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val bookingList = listOf(
        Booking(1, "1", BookingStatus.Scheduled, formatTime.parse("08:00:00")!!, formatTime.parse("12:00:00")!!,),
        Booking(2, "1", BookingStatus.Started, formatTime.parse("14:00:00")!!, formatTime.parse("16:00:00")!!,),
        Booking(3, "1", BookingStatus.Completed, formatTime.parse("18:00:00")!!, formatTime.parse("20:00:00")!!,),
    )
    AndroidArchitectureTheme {
        HomeScreenUI(
            navController = navController,
            customerList = customerList,
            bookingList = bookingList,
            connected = false,
            onLogout = {}
        )
    }
}