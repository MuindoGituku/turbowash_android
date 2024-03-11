package com.ed.turbowash_android.screens.profilemanagement.initialsetup

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ed.turbowash_android.R
import com.ed.turbowash_android.SplashScreenView
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.CustomStepperIndicator
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileSetupScreen(onProfileCreatedSuccessfully: () -> Unit) {
    val customerProfileViewModel: CustomerProfileViewModel = hiltViewModel()
    val profileUploadCompleted by customerProfileViewModel.profileUploadCompleted.collectAsState()
    val uploadIsLoading by customerProfileViewModel.loading.collectAsState()

    var currentStepView by remember { mutableStateOf(ProfileSetupStep.BriefingAndIntro) }

    var showAlert by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }

    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val fullNames = remember { mutableStateOf("") }
    val fullNamesValidationError = remember { mutableStateOf(false) }
    val emailAddress = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.email.toString()) }
    val phoneNumber = remember { mutableStateOf("") }
    val phoneNumberValidationError = remember { mutableStateOf(false) }
    val gender = remember { mutableStateOf("") }
    val genderValidationError = remember { mutableStateOf(false) }
    val selectedDateOfBirth = remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val dateOfBirthText = remember(selectedDateOfBirth.value) {
        mutableStateOf("Selected ${selectedDateOfBirth.value.format(dateFormatter)}")
    }
    val dateOfBirthValidationError = remember { mutableStateOf(false) }

    val homeAddress = remember { mutableStateOf("") }
    val homeAddressValidationError = remember { mutableStateOf(false) }
    val longitude = remember { mutableDoubleStateOf(0.0) }
    val latitude = remember { mutableDoubleStateOf(0.0) }

    val city = remember { mutableStateOf("") }
    val cityValidationError = remember { mutableStateOf(false) }
    val province = remember { mutableStateOf("") }
    val provinceValidationError = remember { mutableStateOf(false) }
    val country = remember { mutableStateOf("") }
    val countryValidationError = remember { mutableStateOf(false) }
    val postalCode = remember { mutableStateOf("") }

    fun isLessThanEighteenYearsFromNow(year: Int, month: Int, dayOfMonth: Int): Boolean {
        val birthDate = LocalDate.of(year, month, dayOfMonth)
        val currentDate = LocalDate.now()
        val age = Period.between(birthDate, currentDate).years
        return age < 18
    }

    fun onGenderSelected(selectedGender: String) {
        gender.value = selectedGender
        showGenderDialog = false
    }

    fun validate(): Boolean {
        var hasError = false

        fullNamesValidationError.value = fullNames.value.isBlank()
        phoneNumberValidationError.value = !phoneNumber.value.matches("^[0-9]{10}$".toRegex()) || phoneNumber.value.isBlank()
        genderValidationError.value = gender.value.isBlank()
        dateOfBirthValidationError.value = isLessThanEighteenYearsFromNow(selectedDateOfBirth.value.year, selectedDateOfBirth.value.monthValue, selectedDateOfBirth.value.dayOfMonth,)
        homeAddressValidationError.value = homeAddress.value.isBlank()
        cityValidationError.value = city.value.isBlank()
        provinceValidationError.value = province.value.isBlank()
        countryValidationError.value = country.value.isBlank()

        if (
            fullNamesValidationError.value || phoneNumberValidationError.value || genderValidationError.value || dateOfBirthValidationError.value ||
            homeAddressValidationError.value || cityValidationError.value || provinceValidationError.value || countryValidationError.value
            ) {
            hasError = true
        }

        return hasError
    }

    fun onProfileCompletedSuccessfully() {
        val dateOfBirth = Date.from(selectedDateOfBirth.value.atStartOfDay(ZoneId.systemDefault()).toInstant())

        customerProfileViewModel.initialProfileUpload(
            fullNames = fullNames.value,
            phoneNumber = phoneNumber.value,
            gender = gender.value,
            dateOfBirth = dateOfBirth,
            profileImage = imageBitmap.value,
            homeAddress = homeAddress.value,
            city = city.value,
            province = province.value,
            country = country.value,
            postalCode = postalCode.value,
            longitude = longitude.doubleValue,
            latitude = latitude.doubleValue
        )
    }

    val currentStep = when (currentStepView) {
        ProfileSetupStep.BriefingAndIntro -> 1
        ProfileSetupStep.PersonalInformation -> 2
        ProfileSetupStep.AddressDetails -> 3
    }
    val totalSteps = 3

    LaunchedEffect(profileUploadCompleted) {
        if (profileUploadCompleted) {
            customerProfileViewModel.resetProfileUploadState()
            onProfileCreatedSuccessfully()
        }
    }

    if (uploadIsLoading) {
        SplashScreenView()
    } else {
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
                            currentStepView = ProfileSetupStep.AddressDetails
                        },
                        onBackClicked = {
                            currentStepView = ProfileSetupStep.BriefingAndIntro
                        },
                        fullNames = fullNames,
                        fullNamesValidationError = fullNamesValidationError,
                        emailAddress = emailAddress,
                        phoneNumber = phoneNumber,
                        phoneNumberValidationError = phoneNumberValidationError,
                        imageBitmap = imageBitmap,
                        gender = gender,
                        genderValidationError = genderValidationError,
                        dateOfBirth = dateOfBirthText,
                        dateOfBirthValidationError = dateOfBirthValidationError,
                        onClickDateField = { showDatePickerDialog = true },
                        onClickGenderField = { showGenderDialog = true },
                    )

                    ProfileSetupStep.AddressDetails -> AddressDetailsStep(
                        onProfileCompleted = {
                            if (!validate()) {
                                onProfileCompletedSuccessfully()
                            } else {
                                currentStepView = ProfileSetupStep.PersonalInformation
                            }
                        },
                        homeAddress = homeAddress,
                        latitude = latitude,
                        longitude = longitude,
                        homeAddressValidationError = homeAddressValidationError,
                        city = city,
                        cityValidationError = cityValidationError,
                        province = province,
                        provinceValidationError = provinceValidationError,
                        country = country,
                        countryValidationError = countryValidationError,
                        postalCode = postalCode,
                        onBackClicked = {
                            currentStepView = ProfileSetupStep.PersonalInformation
                        },
                    )
                }
            }
        }
    }

    if (showDatePickerDialog) {
        ShowDatePicker(
            selectedDate = selectedDateOfBirth.value,
            onDateSelected = { date ->
                selectedDateOfBirth.value = date
                showDatePickerDialog = false
            }
        )
    }

    if (showGenderDialog) {
        GenderAlertDialog(
            onDismissRequest = { showGenderDialog = false },
            onGenderSelected = ::onGenderSelected
        )
    }

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDatePicker(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(true) }

    if (showDialog.value) {
        LaunchedEffect(Unit) {
            val datePickerDialog = android.app.DatePickerDialog(
                context,
                { _, year, monthOfYear, dayOfMonth ->
                    onDateSelected(LocalDate.of(year, monthOfYear + 1, dayOfMonth))
                },
                selectedDate.year,
                selectedDate.monthValue - 1,
                selectedDate.dayOfMonth
            )
            datePickerDialog.setOnDismissListener {
                showDialog.value = false
            }
            datePickerDialog.show()
        }
    }
}

@Composable
fun GenderAlertDialog(
    onDismissRequest: () -> Unit,
    onGenderSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Select Gender") },
        text = {
            Column {
                TextButton(onClick = { onGenderSelected("Male") }) {
                    Text("Male")
                }
                TextButton(onClick = { onGenderSelected("Female") }) {
                    Text("Female")
                }
                TextButton(onClick = { onGenderSelected("Other") }) {
                    Text("Other")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
