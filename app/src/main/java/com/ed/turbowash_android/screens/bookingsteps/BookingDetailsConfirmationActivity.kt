package com.ed.turbowash_android.screens.bookingsteps

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.LargeDisplaySecondaryTopBar
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.viewmodels.SharedInstancesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingDetailsConfirmation(
    customer: Customer,
    sharedInstancesViewModel: SharedInstancesViewModel,
    onClickBackArrow: () -> Unit
) {
    val selectedWashDate = remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val selectedWashDateText = remember(selectedWashDate.value) {
        mutableStateOf("Selected ${selectedWashDate.value.format(dateFormatter)}")
    }

    val selectedService = sharedInstancesViewModel.selectedService.collectAsState().value!!
    val selectedVehicle = sharedInstancesViewModel.selectedVehicle.collectAsState().value
    val selectedAddress = sharedInstancesViewModel.selectedAddress.collectAsState().value
    val selectedCard = sharedInstancesViewModel.selectedPaymentCard.collectAsState().value

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            item {
                LargeDisplaySecondaryTopBar(onClickBack = { onClickBackArrow() }, pageTitle = selectedService.name)
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