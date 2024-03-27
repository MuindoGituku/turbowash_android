package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.models.Service
import com.ed.turbowash_android.R

@Composable
fun CarWashTypeItemView(carWash: Service, onClickService: (carWash: Service) -> Unit) {
    Column(
        modifier = Modifier
            .padding(bottom = 20.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .clickable {
                onClickService(carWash)
            }
            .border(2.dp, colorResource(id = R.color.turboBlue), shape = RoundedCornerShape(10.dp))
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
        ) {
            Text(
                text = carWash.name,
                color = colorResource(id = R.color.turboBlue),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("$ %.2f", carWash.recommendedPrice),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        }
        Text(
            text = carWash.description,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        )
    }
}
