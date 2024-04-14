/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.customwidgets.OnboardingScreenView
import com.ed.turbowash_android.models.OnboardingStep
import com.ed.turbowash_android.repositories.PreferencesRepository
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeWalkThrough(onOnboardingComplete: () -> Unit) {
    val pages = remember { OnboardingStep.samplePages }
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0){ pages.size }
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = true,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val currentPage = pages[page]

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            OnboardingScreenView(page = currentPage)
            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    pageCount = pages.size,
                    modifier = Modifier.padding(20.dp),
                    activeColor = Color.Black,
                    inactiveColor = Color.Gray
                )

                if (page == pages.lastIndex) {
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.dp)) {
                        MaxWidthButton(
                            buttonText = "Get Started",
                            buttonAction = {
                                onOnboardingComplete()
                            },
                            customTextColor = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = colorResource(id = R.color.turboBlue),
                                    shape = RoundedCornerShape(corner = CornerSize(5.dp))
                                )
                        )
                    }
                } else {
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.dp)) {
                        MaxWidthButton(
                            buttonText = "Next",
                            buttonAction = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            customTextColor = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = colorResource(id = R.color.turboBlue),
                                    shape = RoundedCornerShape(corner = CornerSize(5.dp))
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}