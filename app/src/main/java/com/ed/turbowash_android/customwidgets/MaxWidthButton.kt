package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.R

@Composable
fun MaxWidthButton(
    buttonText: String,
    buttonAction: () -> Unit,
    backgroundColor: Color = colorResource(id = R.color.turboBlue),
    customTextColor: Color = Color.Black,
    hasCustomImageColor:Boolean = false,
    customImageColor: Color = colorResource(id = R.color.fadedGray),
    customImageName: Int? = null,
    cornerRadius: Int = 10
) {
    Button(
        onClick = { buttonAction() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(color = backgroundColor),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            if (customImageName != null) {
                Image(
                    painter = painterResource(id = customImageName),
                    contentDescription = null,
                    modifier = Modifier
                        .height(24.dp)
                        .padding(end = 10.dp),
                    colorFilter = if (hasCustomImageColor) ColorFilter.tint(customImageColor) else null
                )
            }
            Text(
                text = buttonText,
                color = customTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMaxWidthButton() {
    MaxWidthButton(
        buttonText = "Preview Button",
        buttonAction = {},
        customTextColor = Color.White,
        cornerRadius = 10,
        customImageName = R.drawable.google_logo
    )
}