package com.ed.turbowash_android.screens.bookingsteps

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.ed.turbowash_android.models.SchedulePeriod
import java.text.SimpleDateFormat
import java.util.*
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomIconClickableField
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.screens.profilemanagement.initialsetup.ShowDatePicker
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun WashDateTimePickerSheet (
    selectedDate: MutableState<Date>,
    selectedPeriod: MutableState<SchedulePeriod?>,
    onConfirmSelection: (Date, SchedulePeriod) -> Unit
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val selectedMutableLocalDate = remember { mutableStateOf(selectedDate.value.toInstant().atZone(
        ZoneId.systemDefault()).toLocalDate()) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val selectedMutableLocalDateText = remember {
        mutableStateOf("Selected ${selectedMutableLocalDate.value.format(dateFormatter)}")
    }
    val dateOfBirthValidationError = remember { mutableStateOf(false) }


    fun convertSpanToTime(span: String): List<Date> {
        val dateFormatter = SimpleDateFormat("ha", Locale.getDefault())

        val timeParts = span.split("-")
        if (timeParts.size != 2) return emptyList()

        val startTime = dateFormatter.parse(timeParts[0])
        val endTime = dateFormatter.parse(timeParts[1])

        val calendar = Calendar.getInstance()
        calendar.time = selectedDate.value

        val startComponents = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedDate.value.year)
            set(Calendar.MONTH, selectedDate.value.month)
            set(Calendar.DAY_OF_MONTH, selectedDate.value.date)
            if (startTime != null) {
                set(Calendar.HOUR_OF_DAY, startTime.hours)
            }
            if (startTime != null) {
                set(Calendar.MINUTE, startTime.minutes)
            }
        }.time

        val endComponents = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedDate.value.year)
            set(Calendar.MONTH, selectedDate.value.month)
            set(Calendar.DAY_OF_MONTH, selectedDate.value.date)
            if (endTime != null) {
                set(Calendar.HOUR_OF_DAY, endTime.hours)
            }
            if (endTime != null) {
                set(Calendar.MINUTE, endTime.minutes)
            }
        }.time

        return listOf(startComponents, endComponents)
    }

    fun canSelectTime(span: String): Boolean {
        val now = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("ha", Locale.getDefault())

        val spanStart = span.split("-").firstOrNull()
        val startTime = dateFormatter.parse(spanStart ?: return false) ?: return false

        val calendar = Calendar.getInstance()
        val selectedDate = Calendar.getInstance().time

        calendar.time = startTime
        calendar.set(Calendar.YEAR, selectedDate.year)
        calendar.set(Calendar.MONTH, selectedDate.month)
        calendar.set(Calendar.DAY_OF_MONTH, selectedDate.day)

        val timeToCheck = calendar.time

        return now <= timeToCheck
    }

    fun generateTimeSpans(): List<String> {
        val formatter = SimpleDateFormat("ha", Locale.getDefault())

        return (6..21).map { hour ->
            val startTime = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, hour) }.time
            val endTime = Calendar.getInstance().apply { time = startTime; add(Calendar.HOUR_OF_DAY, 1) }.time
            "${formatter.format(startTime)}-${formatter.format(endTime)}"
        }
    }

    fun spanMatchesSelectedPeriod(span: String): Boolean {
        if (selectedPeriod.value == null) {
            return false
        } else {
            val twoDateComponents = convertSpanToTime(span)
            if (twoDateComponents.size != 2) {
                return false
            } else {
                val startTime = selectedPeriod.value!!.startTime.toDate()
                val endTime = selectedPeriod.value!!.endTime.toDate()

                return twoDateComponents.first() == startTime && twoDateComponents.last() == endTime
            }
        }
    }

    @Composable
    fun TimeSlot(span: String, onClick: () -> Unit) {

        Text(
            text = span,
            modifier = Modifier
                .padding(5.dp)
                .clickable(enabled = canSelectTime(span), onClick = onClick)
                .background(
                    color = if (spanMatchesSelectedPeriod(span)) colorResource(id = R.color.turboBlue) else if (canSelectTime(span)) Color.White else Color.Gray,
                    shape = RoundedCornerShape(corner = CornerSize(5.dp))
                )
                .padding(15.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                color = if (spanMatchesSelectedPeriod(span)) Color.White else if (canSelectTime(span)) colorResource(id = R.color.turboBlue) else Color.White.copy(alpha = 0.7f),
            ),
            maxLines = 1,
            overflow = TextOverflow.Visible
        )
    }

    fun validateSelectedPeriod(): Boolean {
        var hasError = false

        return hasError
    }

    Column (
        modifier = Modifier
            .fillMaxHeight(0.9f)
    ) {
        Text(
            text = "Select Washing Date",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.W900,
                fontSize = 30.sp
            ),
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 10.dp, top = 20.dp)
        )

        CustomIconClickableField(
            fieldLabel = "Date Of Birth",
            fieldValue = selectedMutableLocalDateText,
            hasValidationError = dateOfBirthValidationError,
            validationErrorText = "You cannot select a date in the past.",
            onClick = { showDatePickerDialog = true },
            fieldIcon = R.drawable.calendar_filled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(generateTimeSpans()) { span ->
                TimeSlot(
                    span = span,
                    onClick = {
                        val timeParts = convertSpanToTime(span)
                        selectedPeriod.value = SchedulePeriod(Timestamp(timeParts.first()), Timestamp(timeParts.last()))

                        Log.d("Schedule", selectedPeriod.value.toString())
                    }
                )
            }
        }

        MaxWidthButton(
            buttonText = "Confirm Selected Period",
            customTextColor = if (selectedPeriod.value != null) Color.White else Color.Black,
            backgroundColor = if (selectedPeriod.value != null) colorResource(
                id = R.color.turboBlue
            ) else colorResource(id = R.color.fadedGray),
            buttonAction = {
                if (!validateSelectedPeriod()) {
                    selectedPeriod.value?.let { onConfirmSelection(selectedDate.value, it) }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .background(
                    color = if (selectedPeriod.value != null) colorResource(
                        id = R.color.turboBlue
                    ) else colorResource(id = R.color.fadedGray),
                    shape = RoundedCornerShape(corner = CornerSize(5.dp))
                )
        )

        if (showDatePickerDialog) {
            ShowDatePicker(
                selectedDate = selectedMutableLocalDate.value,
                onDateSelected = { date ->
                    selectedMutableLocalDate.value = date
                    showDatePickerDialog = false
                }
            )
        }
    }
}