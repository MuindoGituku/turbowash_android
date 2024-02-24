package com.ed.turbowash_android.screens.profilemanagement

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomIconClickableField
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonalInformationStep(
    onContinueClicked: () -> Unit,
    onBackClicked: () -> Unit,
    imageBitmap:MutableState<Bitmap?>,
    fullNames:MutableState<String>,
    fullNamesValidationError:MutableState<Boolean>,
    emailAddress:MutableState<String>,
    phoneNumber:MutableState<String>,
    phoneNumberValidationError:MutableState<Boolean>,
    gender:MutableState<String>,
    genderValidationError:MutableState<Boolean>,
    dateOfBirth:MutableState<String>,
    dateOfBirthValidationError:MutableState<Boolean>,
    onClickGenderField: () -> Unit,
    onClickDateField: () -> Unit,
    ) {

    val modalBottomSheetState = androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
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
    ){
        Column() {
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
                onClick = { onClickGenderField() },
                fieldIcon = R.drawable.gender_filled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            CustomIconClickableField(
                fieldLabel = "Date Of Birth",
                fieldValue = dateOfBirth,
                hasValidationError = dateOfBirthValidationError,
                validationErrorText = "This app is limited to users above the age of 18 years.",
                onClick = { onClickDateField() },
                fieldIcon = R.drawable.calendar_filled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            Row (horizontalArrangement = Arrangement.SpaceBetween) {
                MaxWidthButton(
                    buttonText = "Save and Add Address",
                    buttonAction = {
                        onBackClicked()
                    },
                    customTextColor = Color.White,
                )
                MaxWidthButton(
                    buttonText = "Save and Add Address",
                    buttonAction = {
                        onContinueClicked()
                    },
                    customTextColor = Color.White,
                )
            }
        }
    }
}