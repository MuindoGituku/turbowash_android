package com.ed.turbowash_android.screens.profilemanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.MaxWidthButton

@Composable
fun AddressDetailsStep(
    onProfileCompleted: () -> Unit,
    homeAddress:MutableState<String>,
    homeAddressValidationError:MutableState<Boolean>,
    city:MutableState<String>,
    cityValidationError:MutableState<Boolean>,
    province:MutableState<String>,
    provinceValidationError:MutableState<Boolean>,
    country:MutableState<String>,
    countryValidationError:MutableState<Boolean>,
    postalCode:MutableState<String>,
    ) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val fieldWidth = (screenWidth) / 2

    Column {
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
                validationErrorText = "Provide city name",
                modifier = androidx.compose.ui.Modifier
                    .width(fieldWidth)
                    .padding(start = 10.dp, end = 5.dp),
            )
            CustomIconTextField(
                fieldValue = province,
                fieldLabel = "Province",
                hasValidationError = provinceValidationError,
                validationErrorText = "Provide province (or state)",
                modifier = androidx.compose.ui.Modifier
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
                onProfileCompleted()
            },
            customImageName = R.drawable.person_add_thin,
            customTextColor = Color.White,
            hasCustomImageColor = true,
        )
    }
}