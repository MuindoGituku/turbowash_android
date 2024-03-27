package com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.navigation.NavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.models.Screen
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel

@Composable
fun ViewSavedAddressesScreen(
    customerProfileViewModel: CustomerProfileViewModel,
    navController: NavController
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState()
    val customer = customerProfileViewModel.customerProfile.collectAsState()
    val error = customerProfileViewModel.error.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddSavedAddress.route) }) {
                CustomPaddedIcon(icon = R.drawable.add_address_filled)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Saved Addresses",
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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (customer.value!!.savedAddresses.isNotEmpty()) {
                            items(customer.value!!.savedAddresses, key = { it.tag }) { address ->
                                Text(text = address.address)
                            }
                        } else {
                            item {
                                Text(text = "No saved addresses yet!!")
                            }
                        }
                        item {
                            MaxWidthButton(
                                buttonText = "Add New Address",
                                buttonAction = { },
                                customTextColor = Color.White,
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