/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedvehicles

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.FullWidthLottieAnimWithText
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedVehicle
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import com.google.firebase.Timestamp

@Composable
fun AddSavedVehicleScreen(
    customerProfileViewModel: CustomerProfileViewModel,
    onClickBackArrow: () -> Unit,
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState().value
    val customer = customerProfileViewModel.customerProfile.collectAsState().value!!
    val existingVehicles = customer.savedVehicles
    val error = customerProfileViewModel.error.collectAsState().value

    val context = LocalContext.current

    val vehicleTag = remember { mutableStateOf("") }
    val vehicleTagValidationError = remember { mutableStateOf(false) }
    val vehicleRegNo = remember { mutableStateOf("") }
    val vehicleRegNoValidationError = remember { mutableStateOf(false) }
    val vehicleDesc = remember { mutableStateOf("") }
    val vehicleDescValidationError = remember { mutableStateOf(false) }

    fun validate(): Boolean {
        var hasError = false

        vehicleTagValidationError.value = vehicleTag.value.isEmpty() || existingVehicles.any {
            it.tag == vehicleTag.value
        }
        vehicleRegNoValidationError.value = vehicleRegNo.value.isEmpty() || existingVehicles.any {
            it.regNo == vehicleRegNo.value
        }
        vehicleDescValidationError.value = vehicleDesc.value.isEmpty()

        if (vehicleDescValidationError.value || vehicleRegNoValidationError.value || vehicleTagValidationError.value) {
            hasError = true
        }

        return hasError
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Unspecified,
                elevation = 0.dp,
                contentPadding = PaddingValues(
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 10.dp,
                    top = 20.dp
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.clickable { onClickBackArrow() }) {
                    CustomPaddedIcon(
                        icon = R.drawable.chev_left,
                        backgroundPadding = 5
                    )
                }
                Text(
                    text = "Add New Vehicle",
                    style = TextStyle(
                        fontWeight = FontWeight.W700,
                        fontSize = 27.sp
                    ),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        end = 15.dp
                    )
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            CustomIconTextField(
                fieldValue = vehicleTag,
                fieldIcon = R.drawable.hash_tag,
                fieldLabel = "Vehicle Tag",
                hasValidationError = vehicleTagValidationError,
                validationErrorText = "Please provide a unique tag to remember this vehicle by.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
            )
            CustomIconTextField(
                fieldValue = vehicleRegNo,
                fieldLabel = "Vehicle Registration Number",
                fieldIcon = R.drawable.password_three_star,
                hasValidationError = vehicleRegNoValidationError,
                validationErrorText = "Please provide the correct registration number for your vehicle.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
            )
            CustomIconTextField(
                fieldValue = vehicleDesc,
                fieldLabel = "Wash Instructions",
                hasValidationError = vehicleDescValidationError,
                textInputAutoCap = KeyboardCapitalization.Sentences,
                singleLine = false,
                minLines = 5,
                maxLines = 7,
                validationErrorText = "Please provide any washing instructions to be visible to the cleaner for this vehicle. If none just use N/A.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
            )

            when {
                loadingProfile -> {
                    FullWidthLottieAnimWithText(
                        lottieResource = R.raw.loading,
                        loadingAnimationText = "Uploading new vehicle to your profile on file. Please wait...",
                    )
                }

                !error.isNullOrBlank() -> {
                    MaxWidthButton(
                        buttonText = "Retry Vehicle Upload",
                        buttonAction = {
                            if (!validate()) {
                                val newVehicle = SavedVehicle(
                                    tag = vehicleTag.value,
                                    regNo = vehicleRegNo.value,
                                    description = vehicleDesc.value,
                                    images = mutableListOf()
                                )
                                customerProfileViewModel.addSavedVehicle(newVehicle = newVehicle).also {
                                    Toast.makeText(context, "New Vehicle Added", Toast.LENGTH_LONG)
                                        .show()
                                    onClickBackArrow()
                                }
                            }
                        },
                        backgroundColor = colorResource(id = R.color.turboBlue),
                        customTextColor = colorResource(id = R.color.fadedGray),
                        customImageName = R.drawable.add_card_filled,
                        customImageColor = colorResource(id = R.color.fadedGray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 20.dp)
                            .background(
                                color = colorResource(id = R.color.turboBlue),
                                shape = RoundedCornerShape(corner = CornerSize(5.dp))
                            )
                    )
                }

                else -> {
                    MaxWidthButton(
                        buttonText = "Add New Vehicle",
                        buttonAction = {
                            if (!validate()) {
                                val newVehicle = SavedVehicle(
                                    tag = vehicleTag.value,
                                    regNo = vehicleRegNo.value,
                                    description = vehicleDesc.value,
                                    images = mutableListOf()
                                )
                                customerProfileViewModel.addSavedVehicle(newVehicle = newVehicle).also {
                                    Toast.makeText(context, "New Vehicle Added", Toast.LENGTH_LONG)
                                        .show()
                                    onClickBackArrow()
                                }
                            }
                        },
                        backgroundColor = colorResource(id = R.color.turboBlue),
                        customTextColor = colorResource(id = R.color.fadedGray),
                        customImageName = R.drawable.add_card_filled,
                        customImageColor = colorResource(id = R.color.fadedGray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 20.dp)
                            .background(
                                color = colorResource(id = R.color.turboBlue),
                                shape = RoundedCornerShape(corner = CornerSize(5.dp))
                            )
                    )
                }
            }
        }
    }
}