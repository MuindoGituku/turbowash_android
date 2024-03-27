package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ed.turbowash_android.models.Contract
import com.ed.turbowash_android.R
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CustomContractCard1(contract: Contract, onClickContract: (contract: Contract) -> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .border(1.dp, colorResource(id = R.color.turboBlue), shape = RoundedCornerShape(5.dp))
            .padding(5.dp)
            .clickable {
                onClickContract(contract)
            }
    ) {
        fun formatTimestamp(timestamp: Timestamp): String {
            val sdf = SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
            val date = timestamp.toDate()
            return sdf.format(date)
        }

        Column(
            modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 10.dp, top = 5.dp)
        ) {
            Text(
                text = contract.contractTitle.split("at").first().trim(),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp)
            )
            Divider(
                modifier = Modifier.padding(vertical = 5.dp)
            )
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (contract.provider.imageUrl.isNotEmpty()) {
                    val painter = rememberImagePainter(
                        data = contract.provider.imageUrl,
                        builder = {
                            crossfade(true)
                            placeholder(drawableResId = R.drawable.user_png)
                            error(R.drawable.user_png)
                        }
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Provider Profile Image",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.FillBounds,
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.user_png),
                        contentDescription = "Provider Profile Image",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.FillBounds,
                    )
                }
                Column(
                    modifier = Modifier.padding(start = 15.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = contract.provider.fullNames,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.Black,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.calendar_outline),
                            contentDescription = "Calendar Icon",
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 5.dp),
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(Color.Gray)
                        )
                        Text(
                            text = formatTimestamp(contract.proposedDate),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.loc_pin_outline),
                            contentDescription = "Location Icon",
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 5.dp),
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(Color.Gray)
                        )
                        Text(
                            text = contract.address.city,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
