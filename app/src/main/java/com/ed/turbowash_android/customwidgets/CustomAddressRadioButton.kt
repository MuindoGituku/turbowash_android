package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.ed.turbowash_android.models.SavedAddress

@Composable
fun CustomAddressRadioButton(address: SavedAddress, onTapAddress: (SavedAddress) -> Unit, addressSelected: Boolean) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ){
        Column (modifier = Modifier.weight(1f)) {
            Text(text = address.tag, style = MaterialTheme.typography.h6)
            Text(text = address.address, style = MaterialTheme.typography.body1)
        }
        MaxWidthButton(
            buttonText = if (addressSelected) "Selected" else "Select",
            buttonAction = { if (!addressSelected) onTapAddress(address) },
            customTextColor = if (addressSelected) Color.White else colorResource(id = R.color.turboBlue),
            customImageName = if (addressSelected) R.drawable.check_underlined else null,
            backgroundColor = if (addressSelected) colorResource(id = R.color.turboBlue) else Color.White,
            customImageColor = Color.White,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.turboBlue),
                    shape = RoundedCornerShape(size = 5.dp),
                )
                .background(
                    color = if (addressSelected) colorResource(id = R.color.turboBlue) else Color.White,
                    shape = RoundedCornerShape(size = 5.dp),
                )
        )
    }
}