package com.ed.turbowash_android.screens.profilemanagement

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileSetupScreen(onProfileCreatedSuccessfully: () -> Unit) {

    var showAlert by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val modalBottomSheetState = androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val fieldWidth = (screenWidth) / 2

    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }

    val fullNames = remember { mutableStateOf("") }
    val fullNamesValidationError = remember { mutableStateOf(false) }
    val emailAddress = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser!!.email.toString()) }
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

    fun validate():Boolean {
        var hasError = false

        fullNamesValidationError.value = fullNames.value.isBlank()
        phoneNumberValidationError.value = !phoneNumber.value.matches("^[0-9]{10}$".toRegex())
        homeAddressValidationError.value = homeAddress.value.isBlank()
        cityValidationError.value = city.value.isBlank()
        provinceValidationError.value = province.value.isBlank()
        countryValidationError.value = country.value.isBlank()

        if (fullNamesValidationError.value ||
            phoneNumberValidationError.value ||
            homeAddressValidationError.value ||
            cityValidationError.value ||
            provinceValidationError.value ||
            countryValidationError.value){
            hasError = true
        }

        return hasError
    }

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
                Toast.makeText(context, "Camera permission is required to take photos", Toast.LENGTH_LONG).show()
            }
        }
    )



    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            BottomSheetContent(
                onCameraClick = {
                    // Check permission before launching camera
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) -> {
                            // Permission is already granted
                            takePictureLauncher.launch(null)
                        }
                        else -> {
                            // Request the permission
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                    coroutineScope.launch { modalBottomSheetState.hide() }
                },
                onGalleryClick = {
                    // Launch the gallery chooser directly as it doesn't require specific permissions
                    launchImage.launch("image/*")
                    coroutineScope.launch { modalBottomSheetState.hide() }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 20.dp)
                    .fillMaxWidth()
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
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = "Hello, TurboWasher",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Text(
                        text = "Set up your service provider profile",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Text(
                text = "Personal Information",
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(10.dp)
            )
            CustomIconTextField(
                fieldValue = fullNames,
                fieldLabel = "Full Names",
                fieldIcon = R.drawable.user_circle_thin,
                hasValidationError = fullNamesValidationError,
                validationErrorText = "Please provide your full names in order to proceed",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
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
                    .padding(horizontal = 10.dp),
            )
            Text(
                text = "Primary Address",
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(10.dp)
            )
            CustomIconTextField(
                fieldValue = homeAddress,
                fieldLabel = "Home Address",
                onValueChange = { value ->
                    homeAddress.value = value
                },
                fieldIcon = R.drawable.home_outline,
                hasValidationError = homeAddressValidationError,
                validationErrorText = "Please provide your home address in order to proceed",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
            )
            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                CustomIconTextField(
                    fieldValue = city,
                    fieldLabel = "City",
                    hasValidationError = cityValidationError,
                    validationErrorText = "Please provide your city in order to proceed",
                    modifier = Modifier
                        .width(fieldWidth)
                        .padding(start = 10.dp, end = 5.dp),
                )
                CustomIconTextField(
                    fieldValue = province,
                    fieldLabel = "Province",
                    hasValidationError = provinceValidationError,
                    validationErrorText = "Please provide your province (or state) in order to proceed",
                    modifier = Modifier
                        .width(fieldWidth)
                        .padding(start = 5.dp, end = 10.dp),
                )
            }
            CustomIconTextField(
                fieldValue = country,
                fieldLabel = "Country",
                fieldIcon = R.drawable.globe_thin,
                hasValidationError = countryValidationError,
                validationErrorText = "Please provide your country in order to proceed",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
            )
            CustomIconTextField(
                fieldValue = postalCode,
                fieldLabel = "Postal Code",
                textInputAutoCap = KeyboardCapitalization.Characters,
                fieldIcon = R.drawable.post_outline,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 20.dp),
            )
            MaxWidthButton(
                buttonText = "Create Profile",
                buttonAction = {
                    if (validate()) {
                        TODO()
                    }
                },
                customImageName = R.drawable.person_add_thin,
                customTextColor = Color.White,
                hasCustomImageColor = true,
            )
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

@Composable
fun BottomSheetContent(onCameraClick: () -> Unit, onGalleryClick: () -> Unit) {
    Row(modifier = Modifier.padding(16.dp)) {
        TextButton(
            onClick = onCameraClick,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                CustomPaddedIcon(icon = R.drawable.camera_add_filled)
                Spacer(Modifier.height(8.dp))
                Text("Open Camera")
            }
        }
        Spacer(Modifier.width(15.dp))
        TextButton(
            onClick = onGalleryClick,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                CustomPaddedIcon(icon = R.drawable.photo_1_outline)
                Spacer(Modifier.height(8.dp))
                Text("Browse Gallery")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfileSetupScreenPreview(){
    TurboWash_AndroidTheme {
        ProfileSetupScreen { }
    }
}