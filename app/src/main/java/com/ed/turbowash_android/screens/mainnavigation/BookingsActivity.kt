package com.ed.turbowash_android.screens.mainnavigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackBookingsScreen(
    customerProfileViewModel: CustomerProfileViewModel,
    navController: NavController
    ) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState()
    val customer = customerProfileViewModel.customerProfile.collectAsState()
    val error = customerProfileViewModel.error.collectAsState()

    val tabs = listOf("Scheduled", "Past Bookings")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold {
        Column (
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Bookings",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.W900,
                    fontSize = 30.sp
                ),
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 10.dp, top = 20.dp)
            )

            when {
                loadingProfile.value -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.turboBlue),
                            strokeWidth = 5.dp
                        )
                    }
                }

                !error.value.isNullOrBlank() -> {
                    Text(text = error.value!!, color = Color.Red)
                }

                customer.value != null -> {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title) }
                            )
                        }
                    }
                    HorizontalPager(
                        modifier = Modifier.weight(1f),
                        state = rememberPagerState(pageCount = { tabs.size })
                    ) { page ->
                        selectedTabIndex = page
                        when (selectedTabIndex) {
                            0 -> {
                                Text("Scheduled Tab Content")
                            }
                            1 -> {
                                Text("Past Bookings Tab Content")
                            }
                        }
                    }
                }
            }
        }
    }
}