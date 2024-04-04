package com.ed.turbowash_android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ed.turbowash_android.screens.ContractBookingSuccessActivity
import com.ed.turbowash_android.screens.RootHomeNavigation
import com.ed.turbowash_android.screens.WelcomeWalkThrough
import com.ed.turbowash_android.screens.auth.AuthActivityScreenView
import com.ed.turbowash_android.screens.auth.InvalidCredentialsActivity
import com.ed.turbowash_android.screens.profilemanagement.initialsetup.ProfileSetupScreen
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme
import com.ed.turbowash_android.viewmodels.MainViewModel
import com.ed.turbowash_android.viewmodels.UserAuthenticationViewModel
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.maps_api_key))
        }

        setContent {

            val mainViewModel: MainViewModel by viewModels()
            val userAuthViewModel: UserAuthenticationViewModel by viewModels()

            val navController = rememberNavController()
            val navigationRoute by mainViewModel.navigationRoute.observeAsState()

            TurboWash_AndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (navigationRoute == null) {
                        SplashScreenView()
                    } else {
                        NavHost(
                            navController = navController,
                            startDestination = navigationRoute ?: "splash"
                        ) {
                            composable("splash") { SplashScreenView() }
                            composable("onboarding") {
                                WelcomeWalkThrough() {
                                    mainViewModel.onOnboardingComplete()
                                }
                            }
                            composable("auth") {
                                AuthActivityScreenView(
                                    userAuthViewModel,
                                    onAuthCompletedSuccessfully = {
                                        mainViewModel.onAuthCompletedSuccessfully()
                                    },
                                )
                            }
                            composable("profile") {
                                ProfileSetupScreen {
                                    mainViewModel.onProfileCreatedSuccessfully()
                                }
                            }
                            composable("home") {
                                RootHomeNavigation(
                                    onLogOutCustomer = {
                                        userAuthViewModel.logoutAuthenticatedUser().also {
                                            mainViewModel.onCustomerLogOutSuccessfully()
                                        }
                                    },
                                    onContractUploadSuccess = {
                                        mainViewModel.onContractUploadSuccessfully()
                                    }
                                )
                            }
                            composable("booking_success") {
                                ContractBookingSuccessActivity {
                                    mainViewModel.onNavigateBackHome()
                                }
                            }
                            composable("invalid") {
                                InvalidCredentialsActivity(viewModel = userAuthViewModel) {
                                    mainViewModel.onAuthCompletedSuccessfully()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreenView() {
    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.turbowash_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.turboBlue),
                strokeWidth = 5.dp,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Welcome",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black)
            )
        }

        Spacer(modifier = Modifier.weight(2f))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TurboWash_AndroidTheme {
        SplashScreenView()
    }
}