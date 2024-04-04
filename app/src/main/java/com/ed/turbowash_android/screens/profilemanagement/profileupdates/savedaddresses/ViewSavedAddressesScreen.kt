package com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel

@Composable
fun ViewSavedAddressesScreen(
    customerProfileViewModel: CustomerProfileViewModel,
    onClickBackArrow: () -> Unit,
    onClickAddNewAddress: () -> Unit,
    onClickUpdateSelectedAddress: (SavedAddress) -> Unit,
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState().value
    val customer = customerProfileViewModel.customerProfile.collectAsState().value!!
    val savedAddresses = customer.savedAddresses
    val error = customerProfileViewModel.error.collectAsState().value

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
                    text = "Saved Addresses",
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
            items(savedAddresses, key = { it.tag }) { address ->
                Text(
                    text = address.address,
                    modifier = Modifier.clickable {
                        onClickUpdateSelectedAddress (address)
                    }
                )
            }
            item {
                MaxWidthButton(
                    buttonText = "Add New Address",
                    buttonAction = { onClickAddNewAddress() },
                    customTextColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            }
        }
    }
}