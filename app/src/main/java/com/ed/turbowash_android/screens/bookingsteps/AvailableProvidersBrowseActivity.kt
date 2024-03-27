package com.ed.turbowash_android.screens.bookingsteps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.models.SavedVehicle
import com.ed.turbowash_android.models.Service
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import com.ed.turbowash_android.viewmodels.ProviderProfileViewModel
import java.util.Date

@Composable
fun AvailableProvidersBrowseActivity(
    customerProfileViewModel: CustomerProfileViewModel,
    navController: NavController,
    selectedService: Service,
    selectedAddress: SavedAddress,
    selectedVehicle: SavedVehicle,
    selectedPaymentCard: PaymentCard,
    selectedDate: Date
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState()
    val customer = customerProfileViewModel.customerProfile.collectAsState()
    val error = customerProfileViewModel.error.collectAsState()

    val providerProfileViewModel: ProviderProfileViewModel = hiltViewModel()
    val providersLoading = providerProfileViewModel.loading.collectAsState()
    val providersList = providerProfileViewModel.providersList.collectAsState()
    val providersError = providerProfileViewModel.error.collectAsState()
}