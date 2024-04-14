/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ed.turbowash_android.R
import com.ed.turbowash_android.models.screensList
import com.ed.turbowash_android.models.Screen
import com.ed.turbowash_android.screens.bookingsteps.walkfromhome.AvailableProvidersBrowseActivity
import com.ed.turbowash_android.screens.bookingsteps.walkfromhome.BookingDetailsConfirmation
import com.ed.turbowash_android.screens.detailsscreens.ContractDetailsChatScreen
import com.ed.turbowash_android.screens.detailsscreens.SingleContractDetailsScreen
import com.ed.turbowash_android.screens.mainnavigation.FavoriteHiresScreen
import com.ed.turbowash_android.screens.mainnavigation.HomeLandingScreen
import com.ed.turbowash_android.screens.mainnavigation.MainSettingsScreen
import com.ed.turbowash_android.screens.mainnavigation.TrackBookingsScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.personaldata.ViewCustomerProfileScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.paymentcards.AddPaymentCardScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.paymentcards.UpdatePaymentCardScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.paymentcards.ViewPaymentCardsScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.personaldata.UpdatePersonalDataScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses.AddSavedAddressScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses.UpdateSavedAddressScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses.ViewSavedAddressesScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedvehicles.AddSavedVehicleScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedvehicles.UpdateSavedVehicleScreen
import com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedvehicles.ViewSavedVehiclesScreen
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import com.ed.turbowash_android.viewmodels.SharedInstancesViewModel
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootHomeNavigation(onLogOutCustomer: () -> Unit, onContractUploadSuccess: () -> Unit) {
    val navController = rememberNavController()

    val customerProfileViewModel: CustomerProfileViewModel = hiltViewModel()

    val shouldShowBottomBar =
        navController.currentBackStackEntryAsState().value?.destination?.route in listOf(
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
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)

                    HomeLandingScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        onClickedServiceCard = {
                            sharedInstancesViewModel.updateSelectedService(it)
                            navController.navigate(Screen.BookingDetailsConfirmation.route)
                        },
                        onClickedProfileImage = {
                            navController.navigate(Screen.ViewCustomerProfile.route)
                        }
                    )
                }
                composable(Screen.Activity.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)

                    TrackBookingsScreen(
                        onClickedContractCard = {
                            sharedInstancesViewModel.updateSelectedContractID(it)
                            navController.navigate(Screen.ViewContractDetails.createRoute(it))
                        },
                    )
                }
                composable(Screen.ViewContractDetails.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)
                    val contractInViewID =
                        sharedInstancesViewModel.selectedContractID.collectAsState().value

                    SingleContractDetailsScreen(
                        contractID = contractInViewID,
                        onClickBackArrow = { navController.popBackStack() },
                        onClickChatsButton = {
                            navController.navigate(
                                Screen.ViewContractChatMessages.createRoute(
                                    contractInViewID
                                )
                            )
                        },
                        onClickMapButton = { /*TODO*/ },
                        onClickProviderInfoButton = {
                            sharedInstancesViewModel.updateSelectedProviderID(it)
                        }
                    )
                }

                composable(Screen.ViewContractChatMessages.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)
                    val contractInViewID =
                        sharedInstancesViewModel.selectedContractID.collectAsState().value

                    ContractDetailsChatScreen(
                        contractID = contractInViewID,
                        onClickBackArrow = { navController.popBackStack() },
                    )
                }

                composable(Screen.Favorites.route) {
                    FavoriteHiresScreen(
                        customerProfileViewModel = customerProfileViewModel,
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
                composable(Screen.BookingDetailsConfirmation.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)
                    val customer = customerProfileViewModel.customerProfile.collectAsState().value!!

                    BookingDetailsConfirmation(
                        customer = customer,
                        sharedInstancesViewModel = sharedInstancesViewModel,
                        onClickBackArrow = {
                            navController.popBackStack()
                        },
                        onClickProceedButton = {
                            navController.navigate(Screen.BookingProvidersListBrowse.route)
                        },
                        onClickAddNewCard = {
                            navController.navigate(Screen.AddPaymentCard.route)
                        },
                        onClickAddNewVehicle = {
                            navController.navigate(Screen.AddSavedVehicle.route)
                        },
                        onClickAddNewAddress = {
                            navController.navigate(Screen.AddSavedAddress.route)
                        }
                    )
                }
                composable(Screen.BookingProvidersListBrowse.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)
                    val customer = customerProfileViewModel.customerProfile.collectAsState().value!!

                    val selectedService =
                        sharedInstancesViewModel.selectedService.collectAsState().value!!
                    val selectedVehicle =
                        sharedInstancesViewModel.selectedVehicle.collectAsState().value!!
                    val selectedAddress =
                        sharedInstancesViewModel.selectedAddress.collectAsState().value!!
                    val selectedCard =
                        sharedInstancesViewModel.selectedPaymentCard.collectAsState().value!!
                    val selectedWashPeriod =
                        sharedInstancesViewModel.selectedWashPeriod.collectAsState().value!!
                    val washInstructions =
                        sharedInstancesViewModel.washInstructions.collectAsState().value

                    AvailableProvidersBrowseActivity(
                        customer = customer,
                        selectedService = selectedService,
                        selectedAddress = selectedAddress,
                        selectedVehicle = selectedVehicle,
                        selectedPaymentCard = selectedCard,
                        selectedWashPeriod = selectedWashPeriod,
                        washInstructions = washInstructions,
                        onClickBackArrow = { navController.popBackStack() },
                        onContractUploadSuccess = { onContractUploadSuccess() },
                    )
                }

                //view and manage personal data
                composable(Screen.ViewCustomerProfile.route) { navBackStackEntry ->
                    val customer = customerProfileViewModel.customerProfile.collectAsState().value!!

                    ViewCustomerProfileScreen(
                        customerPersonalData = customer.personalData,
                        onClickBackArrow = { navController.popBackStack() },
                        onClickUpdateButton = {
                            navController.navigate(Screen.UpdateCustomerProfile.route)
                        }
                    )
                }

                composable(Screen.UpdateCustomerProfile.route) { navBackStackEntry ->
                    UpdatePersonalDataScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        onClickBackArrow = { navController.popBackStack() },
                    )
                }

                //manage saved addresses
                composable(Screen.SavedAddressesList.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)

                    ViewSavedAddressesScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        onClickBackArrow = { navController.popBackStack() },
                        onClickAddNewAddress = { navController.navigate(Screen.AddSavedAddress.route) },
                        onClickUpdateSelectedAddress = {
                            sharedInstancesViewModel.updateSelectedAddress(it)
                            navController.navigate(Screen.UpdateSelectedAddress.createRoute(it))
                        }
                    )
                }
                composable(Screen.AddSavedAddress.route) { navBackStackEntry ->
                    AddSavedAddressScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        onClickBackArrow = { navController.popBackStack() },
                    )
                }
                composable(Screen.UpdateSelectedAddress.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)
                    val selectedAddress =
                        sharedInstancesViewModel.selectedAddress.collectAsState().value!!

                    UpdateSavedAddressScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        onClickBackArrow = { navController.popBackStack() },
                        selectedAddress = selectedAddress,
                        onUploadAddressSuccessfully = { }
                    )
                }

                //manage saved vehicles
                composable(Screen.SavedVehiclesList.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)

                    ViewSavedVehiclesScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        onClickBackArrow = { navController.popBackStack() },
                        onClickAddNewVehicle = { navController.navigate(Screen.AddSavedVehicle.route) },
                        onClickUpdateSelectedVehicle = {
                            sharedInstancesViewModel.updateSelectedVehicle(it)
                            navController.navigate(Screen.UpdateSelectedVehicle.createRoute(it))
                        }
                    )
                }
                composable(Screen.AddSavedVehicle.route) {
                    AddSavedVehicleScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        navController = navController
                    )
                }
                composable(Screen.UpdateSelectedVehicle.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)
                    val selectedVehicle =
                        sharedInstancesViewModel.selectedVehicle.collectAsState().value!!

                    UpdateSavedVehicleScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        onClickBackArrow = { navController.popBackStack() },
                        selectedVehicle = selectedVehicle,
                        onUploadVehicleSuccessfully = { }
                    )
                }

                //manage saved payment cards
                composable(Screen.PaymentCardsList.route) { navBackStackEntry ->
                    val sharedInstancesViewModel =
                        navBackStackEntry.sharedViewModel<SharedInstancesViewModel>(navController = navController)

                    ViewPaymentCardsScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        onClickBackArrow = { navController.popBackStack() },
                        onClickAddNewCard = { navController.navigate(Screen.AddPaymentCard.route) },
                        onClickUpdateSelectedCard = {
                            sharedInstancesViewModel.updateSelectedPaymentCard(it)
                            navController.navigate(Screen.UpdateSelectedCard.createRoute(it))
                        }
                    )
                }
                composable(Screen.AddPaymentCard.route) {
                    AddPaymentCardScreen(
                        customerProfileViewModel = customerProfileViewModel,
                        onClickBackArrow = { navController.popBackStack() },
                    )
                }
                composable(Screen.UpdateSelectedCard.route) {
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