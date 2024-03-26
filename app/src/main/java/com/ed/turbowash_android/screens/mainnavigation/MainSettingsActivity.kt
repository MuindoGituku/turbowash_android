package com.ed.turbowash_android.screens.mainnavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomSettingsTile
import com.ed.turbowash_android.customwidgets.CustomUserProfileCard
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.models.Screen
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel


@Composable
fun MainSettingsScreen(
    onLogOutCustomer: () -> Unit,
    customerProfileViewModel: CustomerProfileViewModel,
    navController: NavController
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState()
    val customer = customerProfileViewModel.customerProfile.collectAsState()
    val error = customerProfileViewModel.error.collectAsState()

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.W900,
                    fontSize = 30.sp
                ),
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 10.dp, top = 20.dp)
            )
            when {
                loadingProfile.value -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.turboBlue),
                            strokeWidth = 5.dp
                        )
                    }
                }

                !error.value.isNullOrBlank() -> {
                    Text(text = error.value!!, color = Color.Red)
                }

                customer.value != null -> {
                    LazyColumn() {
                        item {
                            CustomUserProfileCard(
                                onProfileCardClick = { },
                                fullNames = customer.value!!.personalData.fullNames,
                                emailAddress = customer.value!!.personalData.emailAddress.ifEmpty { "(hidden email address)" },
                                phoneNumber = customer.value!!.personalData.phoneNumber,
                                cardPaddingValues = PaddingValues(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 10.dp,
                                    bottom = 25.dp
                                )
                            )
                        }
                        item {
                            CustomSettingsTile(
                                onTileClick = {
                                    navController.navigate(Screen.SavedAddressesList.route)
                                },
                                icon = R.drawable.home_filled,
                                tileTitle = "Saved Addresses",
                                tileSubtitle = "View your saved wash addresses, city, country etc. and make changes to the location...",
                                paddingValues = PaddingValues(
                                    start = 15.dp,
                                    end = 15.dp,
                                    top = 10.dp,
                                    bottom = 15.dp
                                )
                            )
                            CustomSettingsTile(
                                onTileClick = {
                                    navController.navigate(Screen.PaymentCardsList.route)
                                },
                                icon = R.drawable.bank_outline,
                                tileTitle = "Saved Payment Cards",
                                tileSubtitle = "View your saved payment credit and debit cards and make changes, update or delete cards...",
                                paddingValues = PaddingValues(
                                    start = 15.dp,
                                    end = 15.dp,
                                    top = 10.dp,
                                    bottom = 15.dp
                                )
                            )
                            CustomSettingsTile(
                                onTileClick = {
                                    navController.navigate(Screen.SavedVehiclesList.route)
                                },
                                icon = R.drawable.car_outline,
                                tileTitle = "Saved Vehicles",
                                tileSubtitle = "View your saved vehicles under your profile, and make any necessary updates to wash instructions...",
                                paddingValues = PaddingValues(
                                    start = 15.dp,
                                    end = 15.dp,
                                    top = 10.dp,
                                    bottom = 15.dp
                                )
                            )
                        }
                        item {
                            MaxWidthButton(
                                buttonText = "Log Out",
                                buttonAction = { onLogOutCustomer() },
                                customTextColor = Color.White,
                                backgroundColor = Color.Red,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            )
                        }
                    }
                }

                else -> {
                    Text("No profile information available.")
                }
            }
        }
    }
}