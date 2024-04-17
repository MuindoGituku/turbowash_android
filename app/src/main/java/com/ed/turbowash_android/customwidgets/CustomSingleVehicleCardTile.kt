/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.models.SavedVehicle
import com.google.accompanist.pager.HorizontalPagerIndicator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomSingleVehicleTile(
    vehicle: SavedVehicle,
    onTapVehicle: (SavedVehicle) -> Unit,
    outerPaddingValues: PaddingValues = PaddingValues(10.dp),
    innerPaddingValues: PaddingValues = PaddingValues(10.dp)
) {
    val pages = remember { vehicle.images }
    val pagerState =
        androidx.compose.foundation.pager.rememberPagerState(initialPage = 0) { pages.size }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(outerPaddingValues)
            .clickable { onTapVehicle(vehicle) }
    ) {
        Card {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                if (pages.isEmpty()) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.turbowash_logo
                        ),
                        contentDescription = "Vehicle Image"
                    )
                } else {
                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = true,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val currentPage = pages[page]
                        Image(
                            painter = painterResource(
                                id = R.drawable.turbowash_logo
                            ),
                            contentDescription = "Vehicle Image $page"
                        )
                    }
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        pageCount = pages.size,
                        modifier = Modifier.padding(20.dp),
                        activeColor = colorResource(id = R.color.turboBlue),
                        inactiveColor = Color.Gray
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(innerPaddingValues)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
            ) {
                Text(
                    text = vehicle.tag,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = vehicle.regNo, style = MaterialTheme.typography.displaySmall,
                )
            }
            Text(
                text = vehicle.description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}