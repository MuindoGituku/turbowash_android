package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.R

@Composable
fun LargeDisplaySecondaryTopBar(
    onClickBack: () -> Unit,
    pageTitle: String,
    centerTitle: Boolean = true
) {
    Row(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, bottom = 10.dp, top = 20.dp)
            .fillMaxWidth()
            .clickable { onClickBack() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (centerTitle) Arrangement.SpaceBetween else Arrangement.Start,
    ) {
        CustomPaddedIcon(
            icon = R.drawable.chev_left,
            backgroundPadding = 5
        )
        Text(
            text = pageTitle,
            style = TextStyle(
                fontWeight = FontWeight.W700,
                fontSize = 27.sp
            ),
            modifier = Modifier.padding(
                start = 10.dp,
                end = 15.dp
            )
        )
        if (centerTitle) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}