package com.ed.turbowash_android.screens.mainnavigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomSettingsTile
import com.ed.turbowash_android.customwidgets.CustomUserProfileCard
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel


@Composable
fun MainSettingsScreen(onLogOutCustomer: () -> Unit, customerProfileViewModel: CustomerProfileViewModel) {
    val customer = customerProfileViewModel.customerProfile.collectAsState()

    LazyColumn () {
        item {
            customer.value?.personalData?.let {
                CustomUserProfileCard(
                    onProfileCardClick = { },
                    fullNames = it.fullNames,
                    emailAddress = it.emailAddress,
                    phoneNumber = it.phoneNumber,
                )
            }
        }
        item {
            CustomSettingsTile(
                onTileClick = { },
                icon = R.drawable.home_filled,
                tileTitle = "Saved Addresses",
                tileSubtitle = "View your saved home address, city, country etc. and make changes to the location...",
                paddingValues = PaddingValues(
                    start = 15.dp,
                    end = 15.dp,
                    top = 10.dp,
                    bottom = 15.dp
                )
            )
            CustomSettingsTile(
                onTileClick = { },
                icon = R.drawable.bank_outline,
                tileTitle = "Saved Payment Cards",
                tileSubtitle = "View your saved home address, city, country etc. and make changes to the location...",
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
                buttonAction = { onLogOutCustomer() }
            )
        }
    }
}