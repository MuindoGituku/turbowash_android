/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R

@Composable
fun CustomIconClickableField(
    fieldLabel: String,
    fieldValue: MutableState<String>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    fieldIcon: Int? = null,
    disabledField: Boolean = false,
    hasValidationError: MutableState<Boolean>? = null,
    hasInputValidation: Boolean = true,
    validationErrorText: String? = null,
) {
    val borderColor = when {
        hasValidationError?.value == true && hasInputValidation -> Color(0xFFB22222)
        fieldValue.value.isNotEmpty() -> Color(0xFF228B22)
        disabledField -> Color.Gray
        else -> Color.Gray
    }

    Box(
        modifier = modifier
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(4.dp))
            .padding(16.dp)
            .clickable(enabled = !disabledField, onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon
            fieldIcon?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(borderColor),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            // Text
            Text(text = fieldValue.value.ifEmpty { fieldLabel })
        }
    }

    if (hasValidationError?.value == true && hasInputValidation && validationErrorText != null) {
        Row (horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(top = 3.dp)){
            Spacer(modifier = Modifier.width(10.dp))
            androidx.compose.material3.Text(
                text = validationErrorText,
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                color = Color(0xFFB22222),
            )
        }
    }
}

// Usage example with a preview
@Preview(showBackground = true)
@Composable
fun CustomIconClickableFieldPreview() {
    val onClickMock = {} // Placeholder for click action
    val fieldValue = remember { mutableStateOf("") }

    CustomIconClickableField(
        fieldLabel = "Click Me",
        fieldValue = fieldValue,
        onClick = onClickMock,
        fieldIcon = R.drawable.user_circle_thin, // Example icon
        modifier = Modifier.padding(16.dp)
    )
}
