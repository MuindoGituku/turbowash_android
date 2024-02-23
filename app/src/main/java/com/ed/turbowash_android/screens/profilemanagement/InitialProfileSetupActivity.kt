package com.ed.turbowash_android.screens.profilemanagement

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.CustomStepperIndicator
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme
import com.google.firebase.auth.FirebaseAuth

class InitialProfileSetupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TurboWash_AndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileSetupScreen { }
                }
            }
        }
    }
}

@Composable
fun ProfileSetupScreen(onProfileCreatedSuccessfully: () -> Unit) {
    var currentStepView by remember { mutableStateOf(ProfileSetupStep.BriefingAndIntro) }

    var showAlert by remember { mutableStateOf(false) }

    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }

    val fullNames = remember { mutableStateOf("") }
    val fullNamesValidationError = remember { mutableStateOf(false) }
    val emailAddress =
        remember { mutableStateOf(FirebaseAuth.getInstance().currentUser!!.email.toString()) }
    val uid by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser!!.uid) }
    val phoneNumber = remember { mutableStateOf("") }
    val phoneNumberValidationError = remember { mutableStateOf(false) }

    val homeAddress = remember { mutableStateOf("") }
    val homeAddressValidationError = remember { mutableStateOf(false) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var latitude by remember { mutableDoubleStateOf(0.0) }

    val city = remember { mutableStateOf("") }
    val cityValidationError = remember { mutableStateOf(false) }
    val province = remember { mutableStateOf("") }
    val provinceValidationError = remember { mutableStateOf(false) }
    val country = remember { mutableStateOf("") }
    val countryValidationError = remember { mutableStateOf(false) }
    val postalCode = remember { mutableStateOf("") }

    fun validatePersonal(): Boolean {
        var hasError = false

        fullNamesValidationError.value = fullNames.value.isBlank()
        phoneNumberValidationError.value = !phoneNumber.value.matches("^[0-9]{10}$".toRegex()) || phoneNumber.value.isBlank()

        if (fullNamesValidationError.value || phoneNumberValidationError.value) {
            hasError = true
        }

        return hasError
    }

    fun validateAddress(): Boolean {
        var hasError = false

        homeAddressValidationError.value = homeAddress.value.isBlank()
        cityValidationError.value = city.value.isBlank()
        provinceValidationError.value = province.value.isBlank()
        countryValidationError.value = country.value.isBlank()

        if (homeAddressValidationError.value ||
            cityValidationError.value ||
            provinceValidationError.value ||
            countryValidationError.value
        ) {
            hasError = true
        }

        return hasError
    }

    val currentStep = when (currentStepView) {
        ProfileSetupStep.BriefingAndIntro -> 1
        ProfileSetupStep.PersonalInformation -> 2
        ProfileSetupStep.AddressDetails -> 3
    }
    val totalSteps = 3 // Define the total number of steps in the process

    Scaffold(
        topBar = {
            Column {
                Text(
                    text = "Welcome to TurboWash",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 10.dp),
                )
                CustomStepperIndicator(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    circleSize = 12.dp,
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when (currentStepView) {
                ProfileSetupStep.BriefingAndIntro -> InitialTermsOfServiceRead(
                    onContinueClicked = { currentStepView = ProfileSetupStep.PersonalInformation }
                )

                ProfileSetupStep.PersonalInformation -> PersonalInformationStep(
                    onContinueClicked = {
                        if (!validatePersonal()) {
                            showAlert = true
                        } else {
                            currentStepView = ProfileSetupStep.AddressDetails
                        }
                    },
                    fullNames = fullNames,
                    fullNamesValidationError = fullNamesValidationError,
                    emailAddress = emailAddress,
                    phoneNumber = phoneNumber,
                    phoneNumberValidationError = phoneNumberValidationError,
                    imageBitmap = imageBitmap,
                )

                ProfileSetupStep.AddressDetails -> AddressDetailsStep(
                    onProfileCompleted = {
                        if (!validateAddress()) {
                            showAlert = true
                        } else {
                            onProfileCreatedSuccessfully()
                        }
                    },
                    homeAddress = homeAddress,
                    homeAddressValidationError = homeAddressValidationError,
                    city = city,
                    cityValidationError = cityValidationError,
                    province = province,
                    provinceValidationError = provinceValidationError,
                    country = country,
                    countryValidationError = countryValidationError,
                    postalCode = postalCode,
                )
            }
        }
    }


//    uploadSuccess?.let { success ->
//        if (success) {
//            AlertDialog(
//                onDismissRequest = {
//                    viewModel.resetUploadSuccess()
//                    navigateToHome()
//                },
//                title = { Text("Success") },
//                text = { Text("Profile uploaded successfully.") },
//                confirmButton = {
//                    Button(onClick = {
//                        viewModel.resetUploadSuccess()
//                        navigateToHome()
//                    }) {
//                        Text("OK")
//                    }
//                }
//            )
//        } else {
//            AlertDialog(
//                onDismissRequest = {
//                    viewModel.resetUploadSuccess()
//                },
//                title = { Text("Error") },
//                text = { Text("Profile uploading failed. Confirm details and try again!!") },
//                confirmButton = {
//                    Button(onClick = {
//                        viewModel.resetUploadSuccess()
//                    }) {
//                        Text("Try Again")
//                    }
//                }
//            )
//        }
//    }
}

enum class ProfileSetupStep {
    BriefingAndIntro,
    PersonalInformation,
    AddressDetails
}

@Composable
fun BottomSheetContent(onCameraClick: () -> Unit, onGalleryClick: () -> Unit) {
    Row(modifier = Modifier.padding(16.dp)) {
        TextButton(
            onClick = onCameraClick,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CustomPaddedIcon(icon = R.drawable.camera_add_filled)
                Spacer(Modifier.height(8.dp))
                Text("Open Camera")
            }
        }
        Spacer(Modifier.width(15.dp))
        TextButton(
            onClick = onGalleryClick,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CustomPaddedIcon(icon = R.drawable.photo_1_outline)
                Spacer(Modifier.height(8.dp))
                Text("Browse Gallery")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfileSetupScreenPreview() {
    TurboWash_AndroidTheme {
        ProfileSetupScreen { }
    }
}