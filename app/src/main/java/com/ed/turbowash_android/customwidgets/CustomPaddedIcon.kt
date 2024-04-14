/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R

@Composable
fun CustomPaddedIcon(
    cornerRadius:Int = 10,
    backgroundColor: Color = colorResource(id = R.color.turboBlue),
    iconColor: Color = colorResource(id = R.color.fadedGray),
    contentDescription:String = "",
    icon: Int,
    backgroundPadding: Int = 10
) {
    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(cornerRadius.dp))
            .padding(backgroundPadding.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            modifier = Modifier
                .clip(RoundedCornerShape(cornerRadius.dp)),
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomPaddedIcon() {
    CustomPaddedIcon(
        icon = R.drawable.camera_add_filled
    )
}