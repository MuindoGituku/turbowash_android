/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.screens.profilemanagement.initialsetup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.viewmodels.LocationSearchViewModel

@Composable
fun AddressDetailsStep(
    onProfileCompleted: () -> Unit,
    onBackClicked: () -> Unit,
    homeAddress: MutableState<String>,
    homeAddressValidationError: MutableState<Boolean>,
    city: MutableState<String>,
    cityValidationError: MutableState<Boolean>,
    province: MutableState<String>,
    provinceValidationError: MutableState<Boolean>,
    country: MutableState<String>,
    countryValidationError: MutableState<Boolean>,
    postalCode: MutableState<String>,
    longitude: MutableState<Double>,
    latitude: MutableState<Double>
) {

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val screenWidth = configuration.screenWidthDp.dp
    val fieldWidth = (screenWidth) / 2

    val locationViewModel: LocationSearchViewModel = viewModel()
    val locationResults by locationViewModel.locationResults.collectAsState()
    val selectedAddressDetails by locationViewModel.selectedAddressDetails.collectAsState()

    LaunchedEffect(selectedAddressDetails) {
        selectedAddressDetails?.let { details ->
            homeAddress.value = details.name
            city.value = details.city
            province.value = details.state
            country.value = details.country
            postalCode.value = details.postalCode
            latitude.value = details.latitude
            longitude.value = details.longitude
            locationViewModel.updateSearchTerm("")
            focusManager.clearFocus()
        }
    }

    LazyColumn {
        item {
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
                    locationViewModel.updateSearchTerm(value)
                },
                fieldIcon = R.drawable.home_outline,
                hasValidationError = homeAddressValidationError,
                validationErrorText = "Please provide your home address in order to proceed",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
        }
        items(locationResults) { prediction ->
            Column (
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .clickable {
                        locationViewModel.fetchPlaceDetails(prediction.placeId, context)
                    }
            ){
                Text(
                    text = "${prediction.getPrimaryText(null)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                )
                Text(
                    text = "${prediction.getSecondaryText(null)}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.W400,
                        color = Color.Gray,
                        fontSize = 13.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        item {
            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                CustomIconTextField(
                    fieldValue = city,
                    fieldLabel = "City",
                    hasValidationError = cityValidationError,
                    validationErrorText = "Provide city name",
                    modifier = Modifier
                        .width(fieldWidth)
                        .padding(start = 10.dp, end = 5.dp),
                )
                CustomIconTextField(
                    fieldValue = province,
                    fieldLabel = "Province",
                    hasValidationError = provinceValidationError,
                    validationErrorText = "Provide province (or state)",
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
                    .padding(10.dp),
            )
            CustomIconTextField(
                fieldValue = postalCode,
                fieldLabel = "Postal Code",
                fieldIcon = R.drawable.post_outline,
                hasInputValidation = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                MaxWidthButton(
                    buttonText = "Go Back",
                    buttonAction = {
                        onBackClicked()
                    },
                    customTextColor = Color.Black,
                    backgroundColor = colorResource(id = R.color.fadedGray),
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .background(
                            color = colorResource(id = R.color.fadedGray),
                            shape = RoundedCornerShape(corner = CornerSize(5.dp))
                        )
                )
                MaxWidthButton(
                    buttonText = "Create Profile",
                    buttonAction = {
                        onProfileCompleted()
                    },
                    customTextColor = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .background(
                            color = colorResource(id = R.color.turboBlue),
                            shape = RoundedCornerShape(corner = CornerSize(5.dp))
                        )
                )
            }
        }
    }
}