package com.ed.turbowash_android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.models.screensList
import com.ed.turbowash_android.models.Screen
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

@Composable
fun RootHomeNavigation(onLogOutCustomer: () -> Unit) {
    val navController = rememberNavController()
    val customerProfileViewModel: CustomerProfileViewModel = hiltViewModel()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Landing.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Landing.route) {
                HomeLandingScreen(
                    customerProfileViewModel = customerProfileViewModel,
                    navController = navController
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