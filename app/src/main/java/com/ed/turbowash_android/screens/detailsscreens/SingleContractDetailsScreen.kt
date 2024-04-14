/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.screens.detailsscreens

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.ErrorAnimatedScreenWithTryAgain
import com.ed.turbowash_android.customwidgets.FullScreenLottieAnimWithText
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.viewmodels.ContractsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun SingleContractDetailsScreen(
    contractID: String,
    onClickBackArrow: () -> Unit,
    onClickChatsButton: () -> Unit,
    onClickMapButton: () -> Unit,
    onClickProviderInfoButton: (String) -> Unit
) {
    val contractsViewModel: ContractsViewModel = hiltViewModel()
    val contractInView = contractsViewModel.contractInView.collectAsState().value
    val contractLoading = contractsViewModel.loading.collectAsState().value
    val contractError = contractsViewModel.error.collectAsState().value

    LaunchedEffect(key1 = Unit) {
        contractsViewModel.getIndividualContractInfo(contractID)
    }

    fun formatTimestamp(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        val date = timestamp.toDate()
        return sdf.format(date)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Unspecified,
            ) {
                MaxWidthButton(
                    buttonText = "Chat With Washer",
                    buttonAction = { onClickChatsButton() },
                    customTextColor = Color.White,
                    customImageName = R.drawable.chat_bubbles_filled,
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
                title = {
                    Text(
                        text = contractInView?.contractTitle ?: "Contract Details",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                        ),
                    )
                },
                actions = {
                    IconButton(onClick = { onClickChatsButton() }) {
                        CustomPaddedIcon(
                            icon = R.drawable.more_vert_fill,
                            backgroundPadding = 5
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onClickBackArrow() }) {
                        CustomPaddedIcon(
                            icon = R.drawable.chev_left,
                            backgroundPadding = 5
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color.Unspecified,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = colorResource(id = R.color.turboBlue),
                    titleContentColor = Color.Black,
                    actionIconContentColor = colorResource(id = R.color.turboBlue),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 0.dp,
                        end = 5.dp,
                        bottom = 10.dp,
                        top = 10.dp
                    ),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when {
                contractLoading -> {
                    FullScreenLottieAnimWithText(
                        lottieResource = R.raw.loading,
                        loadingAnimationText = "Fetching contract details. Please wait...",
                    )
                }

                !contractError.isNullOrBlank() -> {
                    ErrorAnimatedScreenWithTryAgain(
                        lottieResource = R.raw.doc_error,
                        errorText = "$contractError. Click the button below to retry.",
                        retryAction = { contractsViewModel.getIndividualContractInfo(contractID) },
                        retryButtonText = "Try Again"
                    )
                }

                contractInView != null -> {
                    Text(
                        text = "Contracted Washer",
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
                    Card(
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            if (contractInView.provider.imageUrl.isNotEmpty()) {
                                Image(
                                    painter = rememberImagePainter(
                                        data = contractInView.provider.imageUrl,
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
                                        .size(50.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            ) {
                                Text(
                                    text = contractInView.provider.fullNames,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.calendar_outline),
                                        contentDescription = "Calendar Icon",
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 5.dp),
                                        contentScale = ContentScale.Fit,
                                        colorFilter = ColorFilter.tint(Color.Gray)
                                    )
                                    Text(
                                        text = formatTimestamp(if (contractInView.contractStatus.briefMessage == "Proposed") contractInView.proposedDate else contractInView.confirmedDate),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.loc_pin_outline),
                                        contentDescription = "Location Icon",
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 5.dp),
                                        contentScale = ContentScale.Fit,
                                        colorFilter = ColorFilter.tint(Color.Gray)
                                    )
                                    Text(
                                        text = if (contractInView.contractStatus.briefMessage != "Confirmed") contractInView.address.city else contractInView.address.address,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    onClickProviderInfoButton(contractInView.provider.profileID)
                                }
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = R.drawable.info_circle_outlined
                                    ),
                                    contentDescription = "Info about Provider",
                                    colorFilter = ColorFilter.tint(
                                        color = colorResource(
                                            id = R.color.turboBlue
                                        )
                                    )
                                )
                            }
                        }
                    }
                    Text(
                        text = "Washing Address",
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
                    val mapView = rememberMapViewWithLifecycle()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        mapView.getMapAsync { googleMap ->
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(0.0, 0.0)) // Replace with your desired coordinates
                                    .title("Marker")
                            )
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 10f)) // Replace with your desired zoom level
                        }
                    }
                    Text(
                        text = "Vehicle & Instructions",
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
                    Card(
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            if (contractInView.provider.imageUrl.isNotEmpty()) {
                                Image(
                                    painter = rememberImagePainter(
                                        data = contractInView.provider.imageUrl,
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
                                        .size(50.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            ) {
                                Text(
                                    text = contractInView.provider.fullNames,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.calendar_outline),
                                        contentDescription = "Calendar Icon",
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 5.dp),
                                        contentScale = ContentScale.Fit,
                                        colorFilter = ColorFilter.tint(Color.Gray)
                                    )
                                    Text(
                                        text = formatTimestamp(if (contractInView.contractStatus.briefMessage == "Proposed") contractInView.proposedDate else contractInView.confirmedDate),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.loc_pin_outline),
                                        contentDescription = "Location Icon",
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 5.dp),
                                        contentScale = ContentScale.Fit,
                                        colorFilter = ColorFilter.tint(Color.Gray)
                                    )
                                    Text(
                                        text = if (contractInView.contractStatus.briefMessage != "Confirmed") contractInView.address.city else contractInView.address.address,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    onClickProviderInfoButton(contractInView.provider.profileID)
                                }
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = R.drawable.info_circle_outlined
                                    ),
                                    contentDescription = "Info about Provider",
                                    colorFilter = ColorFilter.tint(
                                        color = colorResource(
                                            id = R.color.turboBlue
                                        )
                                    )
                                )
                            }
                        }
                    }

                    when (contractInView.contractStatus.briefMessage) {
                        "Proposed" -> {
                            MaxWidthButton(
                                buttonText = "Reschedule Contract",
                                buttonAction = { /*TODO*/ },
                                customTextColor = colorResource(id = R.color.turboBlue),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp, vertical = 10.dp)
                                    .border(
                                        2.dp,
                                        color = colorResource(id = R.color.turboBlue),
                                        shape = RoundedCornerShape(corner = CornerSize(5.dp))
                                    ),
                            )
                            MaxWidthButton(
                                buttonText = "Cancel Contract",
                                buttonAction = { /*TODO*/ },
                                backgroundColor = MaterialTheme.colorScheme.background,
                                customTextColor = Color.Red,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp, vertical = 10.dp)
                                    .border(
                                        2.dp,
                                        color = Color.Red,
                                        shape = RoundedCornerShape(corner = CornerSize(5.dp))
                                    ),
                            )
                        }

                        "Confirmed" -> {
                            MaxWidthButton(
                                buttonText = "Reschedule Contract",
                                buttonAction = { /*TODO*/ },
                                customTextColor = colorResource(id = R.color.turboBlue),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp, vertical = 10.dp)
                                    .border(
                                        2.dp,
                                        color = colorResource(id = R.color.turboBlue),
                                        shape = RoundedCornerShape(corner = CornerSize(5.dp))
                                    ),
                            )
                            MaxWidthButton(
                                buttonText = "Cancel Contract",
                                buttonAction = { /*TODO*/ },
                                backgroundColor = MaterialTheme.colorScheme.background,
                                customTextColor = Color.Red,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp, vertical = 10.dp)
                                    .border(
                                        2.dp,
                                        color = Color.Red,
                                        shape = RoundedCornerShape(corner = CornerSize(5.dp))
                                    ),
                            )
                        }

                        "Cancelled" -> {

                        }

                        "Rejected" -> {

                        }

                        "Completed" -> {
                            MaxWidthButton(
                                buttonText = "Review and Tip",
                                buttonAction = { /*TODO*/ },
                                customTextColor = colorResource(id = R.color.turboBlue),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp, vertical = 10.dp)
                                    .border(
                                        2.dp,
                                        color = colorResource(id = R.color.turboBlue),
                                        shape = RoundedCornerShape(corner = CornerSize(5.dp))
                                    ),
                            )
                        }

                        "Billed" -> {
                            MaxWidthButton(
                                buttonText = "Review your Cleaner",
                                buttonAction = { /*TODO*/ },
                                customTextColor = colorResource(id = R.color.turboBlue),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp, vertical = 10.dp)
                                    .border(
                                        2.dp,
                                        color = colorResource(id = R.color.turboBlue),
                                        shape = RoundedCornerShape(corner = CornerSize(5.dp))
                                    ),
                            )
                        } else -> {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            id = View.generateViewId()
        }
    }

    rememberUpdatedState(mapView)

    return mapView
}