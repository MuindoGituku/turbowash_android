package com.ed.turbowash_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.customwidgets.OnboardingScreenView
import com.ed.turbowash_android.models.OnboardingStep
import com.ed.turbowash_android.repositories.PreferencesRepository
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.launch

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TurboWash_AndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {}
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeWalkThrough(preferencesRepository: PreferencesRepository, onOnboardingComplete: () -> Unit) {
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
                                preferencesRepository.setOnboardingComplete()
                                onOnboardingComplete()
                            },
                            customTextColor = Color.White,
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
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}