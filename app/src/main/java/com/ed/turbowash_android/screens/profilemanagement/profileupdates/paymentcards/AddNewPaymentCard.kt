package com.ed.turbowash_android.screens.profilemanagement.profileupdates.paymentcards

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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.viewmodels.CustomerProfileViewModel

@Composable
fun AddPaymentCardScreen(
    customerProfileViewModel: CustomerProfileViewModel,
    onClickBackArrow: () -> Unit,
) {
    val loadingProfile = customerProfileViewModel.loading.collectAsState().value
    val customer = customerProfileViewModel.customerProfile.collectAsState().value!!
    val existingCards = customer.savedPaymentCards
    val error = customerProfileViewModel.error.collectAsState().value

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val screenWidth = configuration.screenWidthDp.dp
    val fieldWidth = (screenWidth) / 2

    val cardTag = remember { mutableStateOf("") }
    val cardTagValidationError = remember { mutableStateOf(false) }
    val cardNumber = remember { mutableStateOf("") }
    val cardNumberValidationError = remember { mutableStateOf(false) }
    val cardName = remember { mutableStateOf("") }
    val cardNameValidationError = remember { mutableStateOf(false) }
    val cvv = remember { mutableStateOf("") }
    val cvvValidationError = remember { mutableStateOf(false) }
    val cardExpiry = remember { mutableStateOf("") }
    val cardExpiryValidationError = remember { mutableStateOf(false) }

    fun validate(): Boolean {
        var hasError = false

        if (cardTagValidationError.value || cardNumberValidationError.value || cvvValidationError.value || cardExpiryValidationError.value) {
            hasError = true
        }

        return hasError
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
                    text = "Add New Card",
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
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            CustomIconTextField(
                fieldValue = cardTag,
                fieldIcon = R.drawable.hash_tag,
                fieldLabel = "Card Tag",
                hasValidationError = cardTagValidationError,
                validationErrorText = "Please provide a unique tag to remember this address by e.g., home, school, work etc.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            CustomIconTextField(
                fieldValue = cardName,
                fieldLabel = "Name on Card",
                fieldIcon = R.drawable.user_circle_thin,
                hasValidationError = cardNameValidationError,
                validationErrorText = "Please provide an address in order to proceed",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            CustomIconTextField(
                fieldValue = cardNumber,
                fieldLabel = "Card Number",
                fieldIcon = R.drawable.credit_card_outline,
                hasValidationError = cardNumberValidationError,
                validationErrorText = "Please provide an address in order to proceed",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                CustomIconTextField(
                    fieldValue = cvv,
                    fieldLabel = "CVV",
                    fieldIcon = R.drawable.password_three_star,
                    hasValidationError = cvvValidationError,
                    validationErrorText = "Provide city name",
                    modifier = Modifier
                        .width(fieldWidth)
                        .padding(start = 10.dp, end = 5.dp),
                )
                CustomIconTextField(
                    fieldValue = cardExpiry,
                    fieldLabel = "Valid Thru",
                    fieldPlaceholder = "MM/YYYY",
                    fieldIcon = R.drawable.calendar_outline,
                    hasValidationError = cardExpiryValidationError,
                    validationErrorText = "Provide province (or state)",
                    modifier = Modifier
                        .width(fieldWidth)
                        .padding(start = 5.dp, end = 10.dp),
                )
            }
            MaxWidthButton(
                buttonText = "Add New Card",
                buttonAction = { },
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