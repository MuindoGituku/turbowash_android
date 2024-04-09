package com.ed.turbowash_android.screens.bookingsteps.walkfromhome

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomIconTextField
import com.ed.turbowash_android.customwidgets.CustomPaddedIcon
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.screens.bookingsteps.WashAddressPickerSheet
import com.ed.turbowash_android.screens.bookingsteps.WashBillCardPickerSheet
import com.ed.turbowash_android.screens.bookingsteps.WashDateTimePickerSheet
import com.ed.turbowash_android.screens.bookingsteps.WashVehiclePickerSheet
import com.ed.turbowash_android.viewmodels.SharedInstancesViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingDetailsConfirmation(
    customer: Customer,
    sharedInstancesViewModel: SharedInstancesViewModel,
    onClickBackArrow: () -> Unit,
    onClickProceedButton: () -> Unit,
    onClickAddNewAddress: () -> Unit,
    onClickAddNewVehicle: () -> Unit,
    onClickAddNewCard: () -> Unit,
) {
    val modalBottomSheetState =
        androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    var currentDisplaySheet by remember { mutableStateOf(BookingDetailsSheets.ShowDateTimePickerSheet) }

    val selectedWashDate = sharedInstancesViewModel.selectedWashDate.collectAsState().value
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'at' hh:mm a")
    val selectedWashDateText = remember(selectedWashDate) {
        mutableStateOf("Selected: ${selectedWashDate.format(dateFormatter)}")
    }

    val selectedService = sharedInstancesViewModel.selectedService.collectAsState().value!!

    val selectedVehicle = sharedInstancesViewModel.selectedVehicle.collectAsState().value
    val selectedVehicleValidationError = remember { mutableStateOf(false) }

    val selectedAddress = sharedInstancesViewModel.selectedAddress.collectAsState().value
    val selectedAddressValidationError = remember { mutableStateOf(false) }

    val selectedCard = sharedInstancesViewModel.selectedPaymentCard.collectAsState().value
    val selectedCardValidationError = remember { mutableStateOf(false) }

    val washInstructions = remember { mutableStateOf("") }
    val washInstructionsValidationError = remember { mutableStateOf(false) }

    fun validateBooking(): Boolean {
        var hasError = false

        selectedVehicleValidationError.value = selectedVehicle == null
        selectedAddressValidationError.value = selectedAddress == null
        selectedCardValidationError.value = selectedCard == null
        washInstructionsValidationError.value = washInstructions.value.trim().isEmpty()

        if (selectedVehicleValidationError.value ||
            selectedAddressValidationError.value ||
            selectedCardValidationError.value ||
            washInstructionsValidationError.value
        ) {
            hasError = true
        }

        return hasError
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            when (currentDisplaySheet) {
                BookingDetailsSheets.ShowDateTimePickerSheet -> WashDateTimePickerSheet {

                }

                BookingDetailsSheets.ShowVehiclePickerSheet -> WashVehiclePickerSheet(
                    vehiclesList = customer.savedVehicles,
                    onVehicleConfirmed = {
                        sharedInstancesViewModel.updateSelectedVehicle(it)
                        coroutineScope.launch{ modalBottomSheetState.hide() }
                    },
                    onClickAddNewVehicle = {
                        coroutineScope.launch{ modalBottomSheetState.hide() }
                        onClickAddNewVehicle()
                    }
                )

                BookingDetailsSheets.ShowAddressPickerSheet -> WashAddressPickerSheet(
                    addressesList = customer.savedAddresses,
                    onAddressConfirmed = {
                        sharedInstancesViewModel.updateSelectedAddress(it)
                        coroutineScope.launch{ modalBottomSheetState.hide() }
                    },
                    onClickAddNewAddress = {
                        coroutineScope.launch{ modalBottomSheetState.hide() }
                        onClickAddNewAddress()
                    }
                )

                BookingDetailsSheets.ShowCardPickerSheet -> WashBillCardPickerSheet(
                    cardsList = customer.savedPaymentCards,
                    onCardConfirmed = {
                        sharedInstancesViewModel.updateSelectedPaymentCard(it)
                        coroutineScope.launch{ modalBottomSheetState.hide() }
                    },
                    onClickAddNewCard = {
                        coroutineScope.launch{ modalBottomSheetState.hide() }
                        onClickAddNewCard()
                    }
                )
            }
        }
    ) {
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
                        text = selectedService.name,
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
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Text(
                        text = "Confirm all the details for the vehicle to be cleaned, when and where to do the cleaning, and which credit/debit card to bill for the service.",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W400,
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier.padding(
                            start = 15.dp, top = 5.dp, end = 15.dp, bottom = 15.dp
                        )
                    )
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(
                                15.dp
                            )
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CustomPaddedIcon(
                            icon = R.drawable.calendar_filled,
                            backgroundPadding = 7
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "When do you need the clean?",
                                style = TextStyle(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                ), modifier = Modifier.padding(bottom = 5.dp)
                            )
                            Text(
                                text = selectedWashDateText.value,
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W400,
                                ),
                            )
                        }
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    currentDisplaySheet =
                                        BookingDetailsSheets.ShowDateTimePickerSheet
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {
                            Text(
                                text = "Change", style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.turboBlue),
                                    textDecoration = TextDecoration.Underline,
                                )
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(
                                15.dp
                            )
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CustomPaddedIcon(
                            icon = R.drawable.car_filled,
                            backgroundPadding = 7,
                            iconColor = if (selectedVehicleValidationError.value) Color.Red else if (selectedVehicle != null) colorResource(
                                id = R.color.fadedGray
                            ) else colorResource(id = R.color.turboBlue),
                            backgroundColor = if (selectedVehicleValidationError.value) colorResource(
                                id = R.color.fadedGray
                            ) else if (selectedVehicle != null) colorResource(id = R.color.turboBlue) else colorResource(
                                id = R.color.fadedGray
                            ),
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Which vehicle do you need cleaned?", style = TextStyle(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                ), modifier = Modifier.padding(bottom = 5.dp)
                            )
                            Text(
                                text = if (selectedVehicleValidationError.value) "Please select a vehicle to be scheduled for cleaning in order to proceed!" else if (selectedVehicle != null) "Selected: ${selectedVehicle.tag} - ${selectedVehicle.regNo}" else "Tap to select a vehicle to schedule for cleaning...",
                                style = TextStyle(
                                    color = if (selectedVehicleValidationError.value) Color.Red else Color.Gray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W400,
                                ),
                            )
                        }
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    currentDisplaySheet =
                                        BookingDetailsSheets.ShowVehiclePickerSheet
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {
                            Text(
                                text = if (selectedVehicle != null) "Change" else "Select",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.turboBlue),
                                    textDecoration = TextDecoration.Underline,
                                )
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(
                                15.dp
                            )
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CustomPaddedIcon(
                            icon = R.drawable.loc_pin_filled,
                            backgroundPadding = 7,
                            iconColor = if (selectedAddressValidationError.value) Color.Red else if (selectedAddress != null) colorResource(
                                id = R.color.fadedGray
                            ) else colorResource(id = R.color.turboBlue),
                            backgroundColor = if (selectedAddressValidationError.value) colorResource(
                                id = R.color.fadedGray
                            ) else if (selectedAddress != null) colorResource(id = R.color.turboBlue) else colorResource(
                                id = R.color.fadedGray
                            ),
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Where do you want the cleaning done?", style = TextStyle(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                ), modifier = Modifier.padding(bottom = 5.dp)
                            )
                            Text(
                                text = if (selectedAddressValidationError.value) "Please select an address for cleaning in order to proceed!" else if (selectedAddress != null) "Selected: ${selectedAddress.tag} - ${selectedAddress.address}" else "Tap to select an address where to do the cleaning...",
                                style = TextStyle(
                                    color = if (selectedAddressValidationError.value) Color.Red else Color.Gray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W400,
                                ),
                            )
                        }
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    currentDisplaySheet =
                                        BookingDetailsSheets.ShowAddressPickerSheet
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {
                            Text(
                                text = if (selectedAddress != null) "Change" else "Select",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.turboBlue),
                                    textDecoration = TextDecoration.Underline,
                                )
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(
                                15.dp
                            )
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CustomPaddedIcon(
                            icon = R.drawable.credit_card_filled,
                            backgroundPadding = 7,
                            iconColor = if (selectedCardValidationError.value) Color.Red else if (selectedCard != null) colorResource(
                                id = R.color.fadedGray
                            ) else colorResource(id = R.color.turboBlue),
                            backgroundColor = if (selectedCardValidationError.value) colorResource(
                                id = R.color.fadedGray
                            ) else if (selectedCard != null) colorResource(id = R.color.turboBlue) else colorResource(
                                id = R.color.fadedGray
                            ),
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Which payment card do you us to bill?", style = TextStyle(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.padding(bottom = 5.dp)
                            )
                            Text(
                                text = if (selectedCardValidationError.value) "Please select a card for billing in order to proceed!" else if (selectedCard != null) "Selected: ${selectedCard.tag} - ${selectedCard.cardNumber}" else "Tap to select a card to be billed for the cleaning service...",
                                style = TextStyle(
                                    color = if (selectedCardValidationError.value) Color.Red else Color.Gray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W400,
                                ),
                            )
                        }
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    currentDisplaySheet = BookingDetailsSheets.ShowCardPickerSheet
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {
                            Text(
                                text = if (selectedCard != null) "Change" else "Select",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.turboBlue),
                                    textDecoration = TextDecoration.Underline,
                                )
                            )
                        }
                    }
                }
                item {
                    CustomIconTextField(
                        fieldLabel = "Wash Instructions",
                        fieldValue = washInstructions,
                        onValueChange = { newValue ->
                            washInstructions.value = newValue
                            sharedInstancesViewModel.updateWashInstructions(newValue)
                        },
                        hasValidationError = washInstructionsValidationError,
                        textInputAutoCap = KeyboardCapitalization.Sentences,
                        validationErrorText = "Please provide some wash instructions to guide the cleaner.",
                        singleLine = false,
                        minLines = 5,
                        maxLines = 7,
                        modifier = Modifier
                            .padding(
                                15.dp
                            )
                            .fillMaxWidth(),
                    )
                }
                item {
                    MaxWidthButton(
                        buttonText = "Browse Washers",
                        customTextColor = if (selectedVehicle != null && selectedAddress != null && selectedCard != null && washInstructions.value.trim()
                                .isNotEmpty()
                        ) Color.White else Color.Black,
                        backgroundColor = if (selectedVehicle != null && selectedAddress != null && selectedCard != null && washInstructions.value.trim()
                                .isNotEmpty()
                        ) colorResource(
                            id = R.color.turboBlue
                        ) else colorResource(id = R.color.fadedGray),
                        buttonAction = {
                            if (!validateBooking()) {
                                onClickProceedButton()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 10.dp)
                            .background(
                                color = if (selectedVehicle != null && selectedAddress != null && selectedCard != null && washInstructions.value
                                        .trim()
                                        .isNotEmpty()
                                ) colorResource(
                                    id = R.color.turboBlue
                                ) else colorResource(id = R.color.fadedGray),
                                shape = RoundedCornerShape(corner = CornerSize(5.dp))
                            )
                    )
                }
            }
        }
    }
}

enum class BookingDetailsSheets {
    ShowDateTimePickerSheet,
    ShowVehiclePickerSheet,
    ShowAddressPickerSheet,
    ShowCardPickerSheet,
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDatePicker(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(true) }

    if (showDialog.value) {
        LaunchedEffect(Unit) {
            val datePickerDialog = DatePickerDialog(
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