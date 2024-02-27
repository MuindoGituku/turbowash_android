package com.ed.turbowash_android.screens.profilemanagement.profileupdates.personaldata

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.ed.turbowash_android.R
import com.ed.turbowash_android.SplashScreenView
import com.ed.turbowash_android.customwidgets.CustomIconClickableField
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.models.PersonalData
import com.ed.turbowash_android.screens.profilemanagement.initialsetup.BottomSheetContent
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdatePersonalDataScreen(personalData: PersonalData) {
    val customerProfileViewModel: CustomerProfileViewModel = hiltViewModel()
    val profileUploadCompleted by customerProfileViewModel.profileUploadCompleted.collectAsState()
    val uploadIsLoading by customerProfileViewModel.loading.collectAsState()

    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val existingProfileUrl = remember { mutableStateOf(personalData.profileImage) }

    val fullNames = remember { mutableStateOf(personalData.fullNames) }
    val fullNamesValidationError = remember { mutableStateOf(false) }

    val emailAddress = remember { mutableStateOf(personalData.emailAddress) }

    val phoneNumber = remember { mutableStateOf(personalData.phoneNumber) }
    val phoneNumberValidationError = remember { mutableStateOf(false) }

    val gender = remember { mutableStateOf(personalData.gender) }
    val genderValidationError = remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }

    val selectedDateOfBirth = remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val dateOfBirthText = remember(selectedDateOfBirth.value) {
        mutableStateOf("Selected ${selectedDateOfBirth.value.format(dateFormatter)}")
    }
    val dateOfBirthValidationError = remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }

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
        phoneNumberValidationError.value =
            !phoneNumber.value.matches("^[0-9]{10}$".toRegex()) || phoneNumber.value.isBlank()
        genderValidationError.value = gender.value.isBlank()
        dateOfBirthValidationError.value = isLessThanEighteenYearsFromNow(
            selectedDateOfBirth.value.year,
            selectedDateOfBirth.value.monthValue,
            selectedDateOfBirth.value.dayOfMonth,
        )

        if (fullNamesValidationError.value || phoneNumberValidationError.value || genderValidationError.value || dateOfBirthValidationError.value) {
            hasError = true
        }

        return hasError
    }

    val modalBottomSheetState =
        androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Define a launcher for taking a picture
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            imageBitmap.value = bitmap
        }
    )

    // Define a launcher for picking an image from the gallery
    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { nonNullUri ->
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, nonNullUri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, nonNullUri)
                ImageDecoder.decodeBitmap(source)
            }
            imageBitmap.value = bitmap
        }
    }

    // Define a launcher for requesting camera permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission granted, launch the camera
                takePictureLauncher.launch(null)
            } else {
                // Handle the case where permission is denied.
                Toast.makeText(
                    context,
                    "Camera permission is required to take photos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )

    if (uploadIsLoading) {
        SplashScreenView()
    } else {
        ModalBottomSheetLayout(
            sheetState = modalBottomSheetState,
            sheetContent = {
                BottomSheetContent(
                    onCameraClick = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) -> {
                                takePictureLauncher.launch(null)
                            }

                            else -> {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                        coroutineScope.launch { modalBottomSheetState.hide() }
                    },
                    onGalleryClick = {
                        launchImage.launch("image/*")
                        coroutineScope.launch { modalBottomSheetState.hide() }
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {

                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        contentAlignment = Alignment.TopStart,
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch { modalBottomSheetState.show() }
                            }
                    ) {
                        if (imageBitmap.value != null) {
                            Image(
                                bitmap = imageBitmap.value!!.asImageBitmap(),
                                contentDescription = "Selected profile image",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.user_png),
                                contentDescription = "Default profile image",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(height = 110.dp, width = 110.dp)
                                    .clip(CircleShape)
                                    .aspectRatio(1f)
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.camera_add_filled),
                            contentDescription = "profile-image",
                            modifier = Modifier
                                .size(height = 40.dp, width = 40.dp)
                                .padding(bottom = 5.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = (10).dp, y = (10).dp)
                        )
                    }
                    CustomIconTextField(
                        fieldValue = fullNames,
                        fieldLabel = "Full Names",
                        fieldIcon = R.drawable.user_circle_thin,
                        hasValidationError = fullNamesValidationError,
                        validationErrorText = "Please provide your full names in order to proceed",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    )
                    CustomIconTextField(
                        fieldValue = emailAddress,
                        fieldLabel = "Email Address",
                        disabledField = true,
                        keyboardType = KeyboardType.Email,
                        textInputAutoCap = KeyboardCapitalization.None,
                        fieldIcon = R.drawable.mail_outline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    )
                    CustomIconTextField(
                        fieldValue = phoneNumber,
                        fieldLabel = "Phone Number",
                        keyboardType = KeyboardType.Phone,
                        textInputAutoCap = KeyboardCapitalization.None,
                        fieldIcon = R.drawable.call_outline,
                        hasValidationError = phoneNumberValidationError,
                        validationErrorText = "Your phone number should be 10 digits, without any spaces, special characters or country codes",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    )
                    CustomIconClickableField(
                        fieldLabel = "Gender",
                        fieldValue = gender,
                        hasValidationError = genderValidationError,
                        validationErrorText = "Please select a gender you identify with in order to proceed.",
                        onClick = { showGenderDialog = true },
                        fieldIcon = R.drawable.gender_filled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    )
                    CustomIconClickableField(
                        fieldLabel = "Date Of Birth",
                        fieldValue = dateOfBirthText,
                        hasValidationError = dateOfBirthValidationError,
                        validationErrorText = "This app is limited to users above the age of 18 years.",
                        onClick = { showDatePickerDialog = true },
                        fieldIcon = R.drawable.calendar_filled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    )
                    MaxWidthButton(
                        buttonText = "Continue",
                        buttonAction = { },
                        customTextColor = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            }
        }
    }
}