/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.ErrorAnimatedScreenWithTryAgain
import com.ed.turbowash_android.customwidgets.FullScreenLottieAnimWithText
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
    val customer = customerProfileViewModel.customerProfile.collectAsState().value
    val error = customerProfileViewModel.error.collectAsState().value

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Unspecified,
            ) {
                MaxWidthButton(
                    buttonText = "Add New Address",
                    buttonAction = { onClickAddNewAddress() },
                    customTextColor = Color.White,
                    customImageName = R.drawable.add_address_filled,
                    customImageColor = colorResource(id = R.color.fadedGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .background(
                            color = colorResource(id = R.color.turboBlue),
                            shape = RoundedCornerShape(corner = CornerSize(5.dp))
                        )
                        .border(
                            2.dp,
                            color = colorResource(id = R.color.turboBlue),
                            shape = RoundedCornerShape(corner = CornerSize(5.dp))
                        ),
                )
            }
        },
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
        when {
            loadingProfile -> {
                FullScreenLottieAnimWithText(
                    lottieResource = R.raw.loading,
                    loadingAnimationText = "Fetching addresses saved under your customer profile. Please wait...",
                )
            }

            !error.isNullOrBlank() -> {
                ErrorAnimatedScreenWithTryAgain(
                    lottieResource = R.raw.doc_error,
                    errorText = "$error. Click the button below to retry.",
                    retryAction = { customerProfileViewModel.refreshCustomerProfile() },
                    retryButtonText = "Try Again"
                )
            }

            customer != null -> {
                if (customer.savedAddresses.isEmpty()) {
                    FullScreenLottieAnimWithText(
                        lottieResource = R.raw.empty_list,
                        loadingAnimationText = "You haven't saved any addresses to your profile. Saved addresses will appear here.",
                    )
                } else {
                    val savedAddresses = customer.savedAddresses

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
                    }
                }
            }

            else -> {
                Text(text = "Something is wrong...")
            }
        }
    }
}