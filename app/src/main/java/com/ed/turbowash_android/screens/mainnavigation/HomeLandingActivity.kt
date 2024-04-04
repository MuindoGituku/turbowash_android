package com.ed.turbowash_android.screens.mainnavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CarWashTypeItemView
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.ErrorAnimatedScreenWithTryAgain
import com.ed.turbowash_android.customwidgets.FullScreenLottieAnimWithText
import com.ed.turbowash_android.models.Service
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import com.ed.turbowash_android.viewmodels.TurboServicesViewModel

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HomeLandingScreen(
    customerProfileViewModel: CustomerProfileViewModel,
    onClickedServiceCard: (Service) -> Unit,
    onClickedProfileImage: () -> Unit
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState()
    val customer = customerProfileViewModel.customerProfile.collectAsState()
    val error = customerProfileViewModel.error.collectAsState()

    val turboServicesViewModel: TurboServicesViewModel = hiltViewModel()
    val turboServicesLoading = turboServicesViewModel.loading.collectAsState()
    val turboServices = turboServicesViewModel.servicesList.collectAsState()
    val turboServicesError = turboServicesViewModel.error.collectAsState()

    val searchTerm = remember { mutableStateOf("") }
    val searchTermValidationError = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = turboServicesViewModel) {
        turboServicesViewModel.getAllTurboServices()
    }

    when {
        loadingProfile.value -> {
            FullScreenLottieAnimWithText(
                lottieResource = R.raw.loading,
                loadingAnimationText = "Fetching your customer profile. Please wait...",
            )
        }

        !error.value.isNullOrBlank() -> {
            ErrorAnimatedScreenWithTryAgain(
                lottieResource = R.raw.doc_error,
                errorText = "$error. Click the button below to retry.",
                retryAction = { customerProfileViewModel.refreshCustomerProfile() },
                retryButtonText = "Try Again"
            )
        }

        customer.value != null -> {
            Scaffold (
                topBar = {
                    TopAppBar (
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
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Hello ${
                                    customer.value!!.personalData.fullNames.split(" ").first()
                                }",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.W900,
                                    fontSize = 30.sp
                                )
                            )
                            Text(
                                text = "Ready to get cleaned up!",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.W700,
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        if (customer.value!!.personalData.profileImage.isNotEmpty()){
                            val painter = rememberImagePainter(
                                data = customer.value!!.personalData.profileImage,
                                builder = {
                                    crossfade(true)
                                    placeholder(drawableResId = R.drawable.user_png)
                                    error(R.drawable.user_png)
                                }
                            )
                            Image(
                                painter = painter,
                                contentDescription = "User profile image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(width = 60.dp, height = 70.dp)
                                    .clip(CircleShape)
                                    .clickable { onClickedProfileImage() }
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.user_png),
                                contentDescription = "User profile image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(width = 60.dp, height = 70.dp)
                                    .clip(CircleShape)
                                    .clickable { onClickedProfileImage() }
                            )
                        }
                    }
                }
            ) { paddingValues ->
                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        CustomIconTextField(
                            fieldLabel = "Try searching for services...",
                            fieldValue = searchTerm,
                            hasValidationError = searchTermValidationError,
                            validationErrorText = "Enter service name to search!",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 20.dp)
                        )
                    }

                    when {
                        turboServicesLoading.value -> {
                            item {
                                FullScreenLottieAnimWithText(
                                    lottieResource = R.raw.loading,
                                    loadingAnimationText = "Fetching services we are currently offering. Please wait...",
                                )
                            }
                        }

                        !turboServicesError.value.isNullOrBlank() -> {
                            item {
                                ErrorAnimatedScreenWithTryAgain(
                                    lottieResource = R.raw.general_error,
                                    errorText = "$turboServicesError. Click the button below to retry.",
                                    retryAction = { turboServicesViewModel.getAllTurboServices() },
                                    retryButtonText = "Try Again"
                                )
                            }
                        }

                        turboServices.value.isEmpty() -> {
                            item {
                                FullScreenLottieAnimWithText(
                                    lottieResource = R.raw.empty_list,
                                    loadingAnimationText = "There are no services currently being offered in the app. Please check back later.",
                                )
                            }
                        }

                        turboServices.value.isNotEmpty() && !turboServicesLoading.value && turboServicesError.value.isNullOrBlank() -> {
                            items(turboServices.value, key = { it.id }) { service ->
                                CarWashTypeItemView(
                                    carWash = service,
                                    onClickService = {
                                        onClickedServiceCard(it)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}