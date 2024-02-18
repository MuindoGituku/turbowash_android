package com.ed.turbowash_android.customwidgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R

@Composable
fun CustomIconTextField(
    fieldLabel: String,
    fieldValue: MutableState<String>,
    modifier: Modifier = Modifier,
    onValueChange: ((String) -> Unit)? = null,
    fieldIcon:Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    disabledField: Boolean = false,
    hasValidationError: MutableState<Boolean>? = null,
    hasInputValidation: Boolean = true,
    validationErrorText: String? = null,
    textInputAutoCap: KeyboardCapitalization = KeyboardCapitalization.Words,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isCurrentlyFocused = remember { mutableStateOf(false) }

    // Observe focus changes
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is FocusInteraction.Focus -> isCurrentlyFocused.value = true
                is FocusInteraction.Unfocus -> isCurrentlyFocused.value = false
            }
        }
    }

    Column(horizontalAlignment = Alignment.Start) {
        OutlinedTextField(
            value = fieldValue.value,
            onValueChange = { newValue ->
                onValueChange?.invoke(newValue) ?: run { fieldValue.value = newValue }
            },
            label = { Text(text = fieldLabel) },
            leadingIcon = if (fieldIcon != null) {
                {
                    Image(
                        painter = painterResource(id = fieldIcon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(determineColor(hasValidationError, hasInputValidation, disabledField, fieldValue.value, isCurrentlyFocused.value))
                    )
                }
            } else null,
            singleLine = true,
            modifier = modifier,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                autoCorrect = false,
                capitalization = textInputAutoCap
            ),
            visualTransformation = VisualTransformation.None,
            enabled = !disabledField,
            isError = hasValidationError?.value == true && hasInputValidation,
            interactionSource = interactionSource,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = determineColor(
                    hasValidationError,
                    hasInputValidation,
                    disabledField,
                    fieldValue.value,
                    isCurrentlyFocused.value
                ),
                unfocusedBorderColor = determineColor(
                    hasValidationError,
                    hasInputValidation,
                    disabledField,
                    fieldValue.value,
                    isCurrentlyFocused.value
                ),
                focusedLabelColor = determineColor(
                    hasValidationError,
                    hasInputValidation,
                    disabledField,
                    fieldValue.value,
                    isCurrentlyFocused.value
                ),
                unfocusedLabelColor = determineColor(
                    hasValidationError,
                    hasInputValidation,
                    disabledField,
                    fieldValue.value,
                    isCurrentlyFocused.value
                ),
                errorBorderColor = determineColor(
                    hasValidationError,
                    hasInputValidation,
                    disabledField,
                    fieldValue.value,
                    isCurrentlyFocused.value
                ),
                errorLabelColor = determineColor(
                    hasValidationError,
                    hasInputValidation,
                    disabledField,
                    fieldValue.value,
                    isCurrentlyFocused.value
                ),
            )
        )
        if (hasValidationError?.value == true && hasInputValidation && validationErrorText != null) {
            Row (horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(top = 3.dp)){
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = validationErrorText,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFB22222),
                )
            }
        }
    }
}

@Composable
private fun determineColor(
    hasValidationError: MutableState<Boolean>?,
    hasInputValidation: Boolean,
    disabledField: Boolean,
    fieldValue: String,
    isFocused: Boolean
): Color {
    return when {
        hasValidationError?.value == true && hasInputValidation -> Color(0xFFB22222)
        disabledField -> Color.Gray
        isFocused -> colorResource(id = R.color.turboBlue)
        fieldValue.isNotEmpty() -> Color(0xFF228B22)
        else -> Color.Gray
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun TemplateOutlinedTextFieldPreview(){
    val fieldValue = mutableStateOf("")

    CustomIconTextField(
        fieldLabel = "Email Address",
        fieldValue = fieldValue,
        fieldIcon = R.drawable.mail_outline,
        modifier = Modifier.fillMaxWidth(),
    )
}