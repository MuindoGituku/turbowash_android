/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.customwidgets


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import com.ed.turbowash_android.R

@Composable
fun CustomStepperIndicator(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = colorResource(id = R.color.turboBlue),
    inactiveColor: Color = colorResource(id = R.color.fadedGray),
    circleSize: Dp = 12.dp
) {
    BoxWithConstraints(modifier = modifier) {
        val width = maxWidth - circleSize

        Canvas(modifier = Modifier.fillMaxWidth()) {
            // Calculate the spacing and line positions
            val stepGap = (width - circleSize * (totalSteps - 1)) / (totalSteps - 1).coerceAtLeast(1)
            val yPosition = size.height / 2

            // Draw inactive lines and circles
            for (i in 0 until totalSteps - 1) {
                val startOffset = Offset(x = stepGap.toPx() * i + circleSize.toPx() / 2, y = yPosition)
                val endOffset = Offset(x = stepGap.toPx() * (i + 1) + circleSize.toPx() / 2, y = yPosition)

                drawLine(
                    color = inactiveColor,
                    start = startOffset,
                    end = endOffset,
                    strokeWidth = 5.dp.toPx()
                )
            }

            // Draw active line
            if (currentStep > 1) {
                drawLine(
                    color = activeColor,
                    start = Offset(circleSize.toPx() / 2, yPosition),
                    end = Offset(stepGap.toPx() * (currentStep - 1) + circleSize.toPx() / 2, yPosition),
                    strokeWidth = 5.dp.toPx()
                )
            }

            // Draw circles
            for (i in 0 until totalSteps) {
                val offset = Offset(x = stepGap.toPx() * i + circleSize.toPx() / 2, y = yPosition)
                drawCircle(
                    color = if (i < currentStep) activeColor else inactiveColor,
                    radius = circleSize.toPx() / 1.5f,
                    center = offset
                )
            }
        }
    }
}


