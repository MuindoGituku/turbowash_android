package com.ed.turbowash_android.screens.bookingsteps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.ed.turbowash_android.models.Service
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel

@Composable
fun BookingDetailsConfirmation(
    customerProfileViewModel: CustomerProfileViewModel,
    navController: NavController,
    selectedService: Service
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState()
    val customer = customerProfileViewModel.customerProfile.collectAsState()
    val error = customerProfileViewModel.error.collectAsState()
}