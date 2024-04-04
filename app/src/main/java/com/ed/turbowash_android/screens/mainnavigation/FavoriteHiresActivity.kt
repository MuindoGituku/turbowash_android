package com.ed.turbowash_android.screens.mainnavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.ErrorAnimatedScreenWithTryAgain
import com.ed.turbowash_android.customwidgets.FullScreenLottieAnimWithText
import com.ed.turbowash_android.customwidgets.FullWidthLottieAnimWithText
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import com.ed.turbowash_android.viewmodels.ProviderProfileViewModel

@OptIn(ExperimentalCoilApi::class)
@Composable
fun FavoriteHiresScreen(
    customerProfileViewModel: CustomerProfileViewModel,
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState().value
    val customer = customerProfileViewModel.customerProfile.collectAsState().value
    val error = customerProfileViewModel.error.collectAsState().value

    val providerProfileViewModel: ProviderProfileViewModel = hiltViewModel()
    val providersLoading = providerProfileViewModel.loading.collectAsState().value
    val providersError = providerProfileViewModel.error.collectAsState().value
    val allHiredProviders = providerProfileViewModel.providersList.collectAsState().value

    LaunchedEffect(key1 = Unit) {
        providerProfileViewModel.getPreviousHiredWashersList()
    }

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
                Text(
                    text = "My Team",
                    style = TextStyle(
                        fontWeight = FontWeight.W900,
                        fontSize = 30.sp
                    ),
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            when {
                loadingProfile -> {
                    item {
                        FullScreenLottieAnimWithText(
                            lottieResource = R.raw.loading,
                            loadingAnimationText = "Managing your customer profile. Please wait...",
                        )
                    }
                }

                !error.isNullOrBlank() -> {
                    item {
                        ErrorAnimatedScreenWithTryAgain(
                            lottieResource = R.raw.doc_error,
                            errorText = "$error. Click the button below to retry.",
                            retryAction = { customerProfileViewModel.refreshCustomerProfile() },
                            retryButtonText = "Try Again"
                        )
                    }
                }

                customer != null -> {
                    if (providersLoading) {
                        item {
                            FullScreenLottieAnimWithText(
                                lottieResource = R.raw.loading,
                                loadingAnimationText = "Fetching list of your previous hires. Please wait...",
                            )
                        }
                    } else if (providersError != null) {
                        item {
                            ErrorAnimatedScreenWithTryAgain(
                                lottieResource = R.raw.general_error,
                                errorText = "$providersError. Click the button below to retry.",
                                retryAction = { providerProfileViewModel.getPreviousHiredWashersList() },
                                retryButtonText = "Try Again"
                            )
                        }
                    } else {
                        if (allHiredProviders.isEmpty()) {
                            item {
                                FullScreenLottieAnimWithText(
                                    lottieResource = R.raw.empty_list,
                                    loadingAnimationText = "You have not hired any washer in the past. Contracted washers will appear here.",
                                )
                            }
                        } else {
                            item {
                                Text(
                                    text = "Saved Favorites",
                                    style = TextStyle(
                                        fontWeight = FontWeight.W400,
                                        fontSize = 18.sp
                                    ),
                                    modifier = Modifier.padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                                )
                            }
                            val favorites = allHiredProviders.filter { provider ->
                                customer.favoriteHires.contains(provider.id)
                            }

                            if (favorites.isNotEmpty()) {
                                item {
                                    LazyRow(
                                        modifier = Modifier.padding(horizontal = 15.dp),
                                        content = {
                                            val groupedFavorites = favorites.groupBy { it.id }
                                            groupedFavorites.forEach { (id, providers) ->
                                                item {
                                                    Column(
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        modifier = Modifier.padding(
                                                            top = 10.dp, bottom = 10.dp, end = 15.dp
                                                        )
                                                    ) {
                                                        Image(
                                                            painter = rememberImagePainter(
                                                                data = providers.first().personalData.profileImage,
                                                                builder = {
                                                                    crossfade(true)
                                                                    placeholder(R.drawable.user_png)
                                                                    error(R.drawable.user_png)
                                                                }
                                                            ),
                                                            contentDescription = "User profile image",
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier
                                                                .size(60.dp)
                                                                .clip(CircleShape)
                                                        )
                                                        Text(
                                                            text = providers.first().personalData.fullNames,
                                                            modifier = Modifier.padding(8.dp),
                                                            color = Color.Black,
                                                            fontWeight = FontWeight.W700,
                                                            fontSize = 14.sp
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            } else {
                                item {
                                    FullWidthLottieAnimWithText(
                                        lottieResource = R.raw.empty_list,
                                        loadingAnimationText = "No washers have been saved to your favorites yet. Tap the heart icon next to a washer to save them to favorites."
                                    )
                                }
                            }

                            item {
                                Text(
                                    text = "All Previous Hires",
                                    style = TextStyle(
                                        fontWeight = FontWeight.W400,
                                        fontSize = 18.sp
                                    ),
                                    modifier = Modifier.padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                                )
                            }

                            val groupedProviders = allHiredProviders.groupBy { it.id }
                            groupedProviders.forEach { (id, providers) ->
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(15.dp)
                                    ) {
                                        if (providers.first().personalData.profileImage.isNotEmpty()) {
                                            Image(
                                                painter = rememberImagePainter(
                                                    data = providers.first().personalData.profileImage,
                                                    builder = {
                                                        crossfade(true)
                                                        placeholder(R.drawable.user_png)
                                                        error(R.drawable.user_png)
                                                    }
                                                ),
                                                contentDescription = "User profile image",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(CircleShape)
                                            )
                                        } else {
                                            Image(
                                                painter = painterResource(id = R.drawable.user_png),
                                                contentDescription = "User profile image",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(CircleShape)
                                            )
                                        }
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 8.dp)
                                        ) {
                                            Text(
                                                text = providers.first().personalData.fullNames,
                                                modifier = Modifier.padding(bottom = 4.dp),
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                            Text(
                                                text = "Washer has been hired for ${providers.size} ${if (providers.size > 1) "contracts" else "contract"}.",
                                                modifier = Modifier.padding(bottom = 4.dp),
                                                color = Color.Black,
                                                fontWeight = FontWeight.W400,
                                                fontSize = 13.sp,
                                                fontStyle = FontStyle.Italic,
                                                maxLines = 1,
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                if (favorites.contains(providers.first())) {
                                                    customerProfileViewModel.removeProviderFromFavorites(
                                                        id
                                                    )
                                                } else {
                                                    customerProfileViewModel.addProviderToFavorites(
                                                        id
                                                    )
                                                }
                                            }
                                        ) {
                                            Image(
                                                painter = painterResource(
                                                    id = if (favorites.contains(providers.first())) R.drawable.heart_check_filled else R.drawable.heart_plus_outline
                                                ),
                                                contentDescription = if (favorites.contains(
                                                        providers.first()
                                                    )
                                                ) "Remove from Favorites" else "Save to Favorites",
                                                colorFilter = ColorFilter.tint(
                                                    color = colorResource(
                                                        id = R.color.turboBlue
                                                    )
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
