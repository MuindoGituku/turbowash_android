/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.screens.profilemanagement.profileupdates.savedaddresses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel
import com.ed.turbowash_android.viewmodels.LocationSearchViewModel

@Composable
fun UpdateSavedAddressScreen(
    customerProfileViewModel: CustomerProfileViewModel,
    selectedAddress: SavedAddress,
    onClickBackArrow: () -> Unit,
    onUploadAddressSuccessfully: () -> Unit,
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState().value
    val customer = customerProfileViewModel.customerProfile.collectAsState().value!!
    val existingAddresses = customer.savedAddresses
    val error = customerProfileViewModel.error.collectAsState().value

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val screenWidth = configuration.screenWidthDp.dp
    val fieldWidth = (screenWidth) / 2

    val locationViewModel: LocationSearchViewModel = viewModel()
    val locationResults by locationViewModel.locationResults.collectAsState()
    val selectedAddressDetails by locationViewModel.selectedAddressDetails.collectAsState()

    val addressTag = remember { mutableStateOf("") }
    val addressTagValidationError = remember { mutableStateOf(false) }
    val address = remember { mutableStateOf("") }
    val addressValidationError = remember { mutableStateOf(false) }
    val longitude = remember { mutableDoubleStateOf(0.0) }
    val latitude = remember { mutableDoubleStateOf(0.0) }
    val city = remember { mutableStateOf("") }
    val cityValidationError = remember { mutableStateOf(false) }
    val province = remember { mutableStateOf("") }
    val provinceValidationError = remember { mutableStateOf(false) }
    val country = remember { mutableStateOf("") }
    val countryValidationError = remember { mutableStateOf(false) }
    val postalCode = remember { mutableStateOf("") }

    fun validate(): Boolean {
        var hasError = false

        addressTagValidationError.value = addressTag.value.isBlank()
        addressValidationError.value = address.value.isBlank()
        cityValidationError.value = city.value.isBlank()
        provinceValidationError.value = province.value.isBlank()
        countryValidationError.value = country.value.isBlank()

        if (addressTagValidationError.value || addressValidationError.value || cityValidationError.value || provinceValidationError.value || countryValidationError.value) {
            hasError = true
        }

        return hasError
    }

    LaunchedEffect(selectedAddressDetails) {
        selectedAddressDetails?.let { details ->
            address.value = details.name
            city.value = details.city
            province.value = details.state
            country.value = details.country
            postalCode.value = details.postalCode
            latitude.doubleValue = details.latitude
            longitude.doubleValue = details.longitude
            locationViewModel.updateSearchTerm("")
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(key1 = Unit) {
        addressTag.value = selectedAddress.tag
        address.value = selectedAddress.address
        city.value = selectedAddress.city
        province.value = selectedAddress.province
        country.value = selectedAddress.country
        postalCode.value = selectedAddress.postalCode
        latitude.doubleValue = selectedAddress.coordinates.latitude
        longitude.doubleValue = selectedAddress.coordinates.longitude
    }

    Scaffold (
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
                    text = "Update Address",
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
    ) {
        LazyColumn (
            modifier = Modifier.padding(it)
        ){
            item {
                CustomIconTextField(
                    fieldValue = addressTag,
                    fieldLabel = "Address Tag",
                    hasValidationError = addressTagValidationError,
                    validationErrorText = "Please provide a unique tag to remember this address by e.g., home, school, work etc.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                )
                CustomIconTextField(
                    fieldValue = address,
                    fieldLabel = "Address",
                    onValueChange = { value ->
                        address.value = value
                        locationViewModel.updateSearchTerm(value)
                    },
                    fieldIcon = R.drawable.home_outline,
                    hasValidationError = addressValidationError,
                    validationErrorText = "Please provide an address in order to proceed",
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
                    validationErrorText = "Please provide a country in order to proceed",
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
                MaxWidthButton(
                    buttonText = "Update Address",
                    buttonAction = {
                        if (!validate()){

                        }
                    },
                    customTextColor = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}