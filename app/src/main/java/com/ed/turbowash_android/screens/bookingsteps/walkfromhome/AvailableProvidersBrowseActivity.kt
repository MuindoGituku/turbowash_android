/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.screens.bookingsteps.walkfromhome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.models.SavedVehicle
import com.ed.turbowash_android.models.ScheduleLocal
import com.ed.turbowash_android.models.SchedulePeriod
import com.ed.turbowash_android.models.Service
import com.ed.turbowash_android.viewmodels.ContractsViewModel
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import com.ed.turbowash_android.viewmodels.ProviderProfileViewModel
import java.util.Date

@Composable
fun AvailableProvidersBrowseActivity(
    customer: Customer,
    selectedService: Service,
    selectedAddress: SavedAddress,
    selectedVehicle: SavedVehicle,
    selectedPaymentCard: PaymentCard,
    washInstructions: String,
    selectedWashPeriod: ScheduleLocal,
    onClickBackArrow: () -> Unit,
    onContractUploadSuccess: () -> Unit
) {
    val providerProfileViewModel: ProviderProfileViewModel = hiltViewModel()
    val providersLoading = providerProfileViewModel.loading.collectAsState()
    val providersList = providerProfileViewModel.providersList.collectAsState()
    val providersError = providerProfileViewModel.error.collectAsState()

    LaunchedEffect(key1 = providerProfileViewModel) {
        providerProfileViewModel.getFilteredProvidersWithRegionAndSchedule(
            selectedWashPeriod,
            selectedAddress.city,
            selectedAddress.province,
            selectedService.id
        )
    }

    val contractsViewModel: ContractsViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Unspecified,
                elevation = 0.dp,
                contentPadding = PaddingValues(
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 10.dp,
                    top = 20.dp
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.clickable { onClickBackArrow() }) {
                    CustomPaddedIcon(
                        icon = R.drawable.chev_left,
                        backgroundPadding = 5
                    )
                }
                Text(
                    text = "Browse Available Washers",
                    style = TextStyle(
                        fontWeight = FontWeight.W700,
                        fontSize = 27.sp
                    ),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        end = 15.dp
                    )
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

        }
    }
}