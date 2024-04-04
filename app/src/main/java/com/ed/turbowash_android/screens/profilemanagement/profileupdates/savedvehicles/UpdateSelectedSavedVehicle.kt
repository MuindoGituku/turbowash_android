package com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedvehicles

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.models.SavedVehicle
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel

@Composable
fun UpdateSavedVehicleScreen(
    customerProfileViewModel: CustomerProfileViewModel,
    selectedVehicle: SavedVehicle,
    onClickBackArrow: () -> Unit,
    onUploadVehicleSuccessfully: () -> Unit,
) {}