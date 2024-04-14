/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme

@Composable
fun CustomSettingsTile(
    onTileClick: () -> Unit,
    paddingValues: PaddingValues,
    icon: Int,
    tileContentSpacing: Dp = 10.dp,
    tileTitle: String,
    tileTitleStyle:TextStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Black
    ),
    tileTitleSpacing: Dp = 3.dp,
    tileSubtitle: String,
    tileSubtitleStyle: TextStyle = MaterialTheme.typography.bodySmall,
    cornerRadius:Int = 7,
    backgroundColor: Color = colorResource(id = R.color.turboBlue),
    iconColor: Color = colorResource(id = R.color.fadedGray),
    contentDescription:String = "",
    backgroundPadding: Int = 7
){
    Row (
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .clickable { onTileClick() }
    ){
        CustomPaddedIcon(
            icon = icon,
            backgroundColor = backgroundColor,
            iconColor = iconColor,
            contentDescription = contentDescription,
            backgroundPadding = backgroundPadding,
            cornerRadius = cornerRadius
        )
        Column (
            modifier = Modifier
                .weight(1f)
                .padding(start = tileContentSpacing)
        ){
            Text(
                text = tileTitle,
                style = tileTitleStyle,
                modifier = Modifier.padding(bottom = tileTitleSpacing)
            )
            Text(
                text = tileSubtitle,
                style = tileSubtitleStyle,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomSettingsTilePreview(){
    TurboWash_AndroidTheme {
        Column {
            CustomSettingsTile(
                onTileClick = { },
                icon = R.drawable.home_filled,
                tileTitle = "Home Address",
                tileSubtitle = "View your saved home address, city, country etc. and make changes to the location...",
                paddingValues = PaddingValues(
                    start = 15.dp,
                    end = 15.dp,
                    top = 10.dp,
                    bottom = 15.dp
                )
            )
            CustomSettingsTile(
                onTileClick = { },
                icon = R.drawable.bank_outline,
                tileTitle = "Payment Cards",
                tileSubtitle = "View your saved home address, city, country etc. and make changes to the location...",
                paddingValues = PaddingValues(
                    start = 15.dp,
                    end = 15.dp,
                    top = 10.dp,
                    bottom = 15.dp
                )
            )
        }
    }
}