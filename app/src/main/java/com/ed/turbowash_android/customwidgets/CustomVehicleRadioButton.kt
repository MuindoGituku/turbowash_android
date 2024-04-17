package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.models.SavedVehicle

@Composable
fun CustomVehicleRadioButton(vehicle: SavedVehicle, onTapVehicle: (SavedVehicle) -> Unit, vehicleSelected: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column (modifier = Modifier.weight(1f)) {
            Text(text = vehicle.regNo, style = MaterialTheme.typography.h5)
            Text(text = vehicle.tag, style = MaterialTheme.typography.h6)
        }
        MaxWidthButton(
            buttonText = if (vehicleSelected) "Selected" else "Select",
            buttonAction = { if (!vehicleSelected) onTapVehicle(vehicle) },
            customTextColor = if (vehicleSelected) Color.White else colorResource(id = R.color.turboBlue),
            backgroundColor = if (vehicleSelected) colorResource(id = R.color.turboBlue) else Color.White,
            customImageName = if (vehicleSelected) R.drawable.check_underlined else null,
            customImageColor = Color.White,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.turboBlue),
                    shape = RoundedCornerShape(size = 5.dp),
                )
                .background(
                    color = if (vehicleSelected) colorResource(id = R.color.turboBlue) else Color.White,
                    shape = RoundedCornerShape(size = 5.dp),
                )
        )
    }
}