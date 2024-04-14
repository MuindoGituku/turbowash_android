/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.screens.profilemanagement.profileupdates.paymentcards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.CustomSinglePaymentCardTile
import com.ed.turbowash_android.customwidgets.ErrorAnimatedScreenWithTryAgain
import com.ed.turbowash_android.customwidgets.FullScreenLottieAnimWithText
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel

@Composable
fun ViewPaymentCardsScreen(
    customerProfileViewModel: CustomerProfileViewModel,
    onClickBackArrow: () -> Unit,
    onClickAddNewCard: () -> Unit,
    onClickUpdateSelectedCard: (PaymentCard) -> Unit,
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
                    buttonText = "Add New Card",
                    buttonAction = { onClickAddNewCard() },
                    customTextColor = Color.White,
                    customImageName = R.drawable.add_card_filled,
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
                    text = "Payment Methods",
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
                    loadingAnimationText = "Fetching your customer profile. Please wait...",
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
                Column (
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ){
                    if (customer.savedPaymentCards.isEmpty()) {
                        FullScreenLottieAnimWithText(
                            lottieResource = R.raw.empty_list,
                            loadingAnimationText = "You haven't saved any payment cards to your profile. Saved cards will appear here.",
                        )
                    } else {
                        val savedCards = customer.savedPaymentCards

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            items(savedCards, key = { it.tag }) { card ->
                                CustomSinglePaymentCardTile(
                                    card = card,
                                    onTapUpdate = { onClickUpdateSelectedCard(card) },
                                    onTapDelete = { /*TODO*/ },
                                )
                            }
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