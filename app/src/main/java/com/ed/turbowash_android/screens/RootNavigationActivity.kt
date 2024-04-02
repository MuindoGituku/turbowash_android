package com.ed.turbowash_android.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ed.turbowash_android.R
import com.ed.turbowash_android.models.screensList
import com.ed.turbowash_android.models.Screen
import com.ed.turbowash_android.screens.bookingsteps.BookingDetailsConfirmation
import com.ed.turbowash_android.screens.mainnavigation.FavoriteHiresScreen
import com.ed.turbowash_android.screens.mainnavigation.HomeLandingScreen
import com.ed.turbowash_android.screens.mainnavigation.MainSettingsScreen
import com.ed.turbowash_android.screens.mainnavigation.TrackBookingsScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.paymentcards.AddPaymentCardScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.paymentcards.UpdatePaymentCardScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.paymentcards.ViewPaymentCardsScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses.AddSavedAddressScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses.UpdateSavedAddressScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses.ViewSavedAddressesScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedvehicles.AddSavedVehicleScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedvehicles.UpdateSavedVehicleScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedvehicles.ViewSavedVehiclesScreen
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import com.ed.turbowash_android.viewmodels.SharedInstancesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootHomeNavigation(onLogOutCustomer: () -> Unit) {
    val navController = rememberNavController()

    val customerProfileViewModel: CustomerProfileViewModel = hiltViewModel()

    val shouldShowBottomBar = navController.currentBackStackEntryAsState().value?.destination?.route in listOf(
        Screen.Landing.route,
        Screen.Activity.route,
        Screen.Favorites.route,
        Screen.Settings.route
    )

    Scaffold(
        bottomBar = { if (shouldShowBottomBar) BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation(startDestination = Screen.Landing.route, route = "main") {
                //bottom navigation screens
                composable(Screen.Landing.route) { navBackStackEntry ->
                    val sharedInstancesViewModel = navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)

                    HomeLandingScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController,
                        onClickedServiceCard = {
                            sharedInstancesViewModel.updateSelectedService(it)
                            navController.navigate(Screen.BookingDetailsConfirmation.route)
                        }
                    )
                }
                composable(Screen.Activity.route) {
                    TrackBookingsScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
                composable(Screen.Favorites.route) {
                    FavoriteHiresScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
                composable(Screen.Settings.route) {
                    MainSettingsScreen(
                        onLogOutCustomer = onLogOutCustomer,
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }

                //booking steps screens
                composable(Screen.BookingDetailsConfirmation.route) {navBackStackEntry ->
                    val sharedInstancesViewModel = navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)
                    val customer = customerProfileViewModel.customerProfile.collectAsState().value!!

                    BookingDetailsConfirmation(
                        customer = customer,
                        sharedInstancesViewModel = sharedInstancesViewModel,
                        onClickBackArrow = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(Screen.BookingProvidersListBrowse.route) {navBackStackEntry ->
                    val sharedInstancesViewModel = navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)
                }

                //manage saved addresses
                composable(Screen.SavedAddressesList.route) {
                    ViewSavedAddressesScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
                composable(Screen.AddSavedAddress.route) {
                    AddSavedAddressScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
                composable(Screen.SavedVehiclesList.route) {
                    UpdateSavedAddressScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }

                //manage saved vehicles
                composable(Screen.SavedVehiclesList.route) {
                    ViewSavedVehiclesScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
                composable(Screen.AddSavedVehicle.route) {
                    AddSavedVehicleScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
                composable(Screen.SavedVehiclesList.route) {
                    UpdateSavedVehicleScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }

                //manage saved payment cards
                composable(Screen.PaymentCardsList.route) {
                    ViewPaymentCardsScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
                composable(Screen.AddPaymentCard.route) {
                    AddPaymentCardScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
                composable(Screen.PaymentCardsList.route) {
                    UpdatePaymentCardScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = Color.White,
    ) {
        val currentRoute = currentRoute(navController)
        screensList.forEach { screen ->
            BottomNavigationItem(
                modifier = Modifier.padding(vertical = 5.dp),
                icon = {
                    Image(
                        painterResource(
                            id = if (currentRoute == screen.route) screen.selectedIcon else screen.unselectedIcon
                        ),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            if (currentRoute == screen.route) colorResource(
                                id = R.color.turboBlue
                            ) else Color.Gray
                        ),
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                },
                label = {
                    Text(
                        text = screen.screenTitle,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.W700,
                            color = if (currentRoute == screen.route) colorResource(id = R.color.turboBlue) else Color.Gray
                        )
                    )
                },
                alwaysShowLabel = currentRoute == screen.route,
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}