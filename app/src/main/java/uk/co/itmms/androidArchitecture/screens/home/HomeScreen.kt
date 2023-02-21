package uk.co.itmms.androidArchitecture.screens.home

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
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
import uk.co.itmms.androidArchitecture.R
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    viewModel.state.observeAsState(HomeViewModel.State()).value.apply {
        HomeScreenUI(
            navController = navController,
            startDestinationName = BottomBarRoute.getInitialRoute().routeName,
            currentDestination = currentDestination,
            state = this,
            onEvent = viewModel::doEvent,
            onBottomBarClick = { route ->
                navController.navigate(route.routeName) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
        )
    }
}

@Composable
fun HomeScreenUI(
    navController: NavHostController,
    startDestinationName: String,
    currentDestination: NavDestination?,
    state: HomeViewModel.State,
    onEvent: (HomeViewModel.EventType) -> Unit,
    onBottomBarClick: (BottomBarRoute) -> Unit,
) {
    val connectedText = if (!state.data.connected) stringResource(id = R.string.connectionMissing) else ""

    BackHandler {
        onEvent(HomeViewModel.EventType.Logout)
    }
    
    Scaffold(
        topBar = {
            HomeTopBar(
                connectedText = connectedText,
                onLogout = { onEvent(HomeViewModel.EventType.Logout) },
            )
        },
        bottomBar = {
            BottomBar(
                currentDestination = currentDestination,
                onClick = { onBottomBarClick(it) },
            )
        }
    ) { padding ->
        BottomNavigationHost(
            modifier = Modifier
                .padding(padding),
            navController = navController,
            startDestinationName = startDestinationName,
            customerList = state.data.customerList,
            bookingList = state.data.bookingList,
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
    currentDestination: NavDestination?,
    onClick: (BottomBarRoute) -> Unit,
) {
    val screens = listOf(
        BottomBarScreen.Bookings,
        BottomBarScreen.Customers,
    )

    BottomNavigation {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                onClick = onClick,
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    onClick: (BottomBarRoute) -> Unit,
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
        onClick = { onClick(screen.route) },
    )
}

@Composable
fun BottomNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestinationName: String,
    customerList: List<Customer>,
    bookingList: List<Booking>,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestinationName,
    ) {
        composable(route = BottomBarRoute.Bookings.routeName) { HomeBookingsScreen(bookingList) }
        composable(route = BottomBarRoute.Customers.routeName) { HomeCustomersScreen(customerList) }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenUICustomersPreview() {
    val navController = rememberNavController()
    navController.findDestination(BottomBarRoute.Customers.routeName)?.let {
        navController.navigate(it.id)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AndroidArchitectureTheme {
        HomeScreenUI(
            navController = navController,
            startDestinationName = BottomBarRoute.Customers.routeName,
            currentDestination = currentDestination,
            state = state,
            onEvent = {},
            onBottomBarClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenUIBookingsPreview() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AndroidArchitectureTheme {
        HomeScreenUI(
            navController = navController,
            startDestinationName = BottomBarRoute.Bookings.routeName,
            currentDestination = currentDestination,
            state = state,
            onEvent = {},
            onBottomBarClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenUIDisconnectedPreview() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AndroidArchitectureTheme {
        HomeScreenUI(
            navController = navController,
            startDestinationName = BottomBarRoute.Customers.routeName,
            currentDestination = currentDestination,
            state = state.copy(data = stateData.copy(connected = false)),
            onEvent = {},
            onBottomBarClick = {},
        )
    }
}

private val state: HomeViewModel.State by lazy {
    HomeViewModel.State(
        data = stateData,
    )
}

private val stateData: HomeViewModel.StateData by lazy {
    HomeViewModel.StateData(
        connected = true,
        customerList = customerList,
        bookingList = bookingList,
    )
}

private val customerList: List<Customer> by lazy {
    listOf(
        Customer("1", "Mr. John", "25 Oxford Circus, London"),
        Customer("2", "Mrs. Jane", "4 Piccadilly Circus, London"),
        Customer("3", "Mr. Paul", "12 Piccadilly Circus, London"),
    )
}

private val bookingList: List<Booking> by lazy {
    val formatTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    listOf(
        Booking(1, "1", BookingStatus.Scheduled, formatTime.parse("08:00:00")!!, formatTime.parse("12:00:00")!!,),
        Booking(2, "1", BookingStatus.Started, formatTime.parse("14:00:00")!!, formatTime.parse("16:00:00")!!,),
        Booking(3, "1", BookingStatus.Completed, formatTime.parse("18:00:00")!!, formatTime.parse("20:00:00")!!,),
    )
}