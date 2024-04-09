package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.models.PaymentCard
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomSinglePaymentCardTile(
    card: PaymentCard,
    onTapUpdate: () -> Unit,
    onTapDelete: () -> Unit,
    outerPaddingValues: PaddingValues = PaddingValues(10.dp),
    innerPaddingValues: PaddingValues = PaddingValues(10.dp)
) {
    fun formatTimestamp(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val date = timestamp.toDate()
        return sdf.format(date)
    }

    fun maskedCardNumber(number: String): String {
        return if (number.length >= 4) {
            "**** **** **** ${number.takeLast(4)}"
        } else {
            number
        }
    }

    val modalBottomSheetState =
        androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            BottomSheetContent(
                onTapDelete = { onTapDelete() },
                onTapUpdate = { onTapUpdate() }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(outerPaddingValues)
                .height(200.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            colorResource(id = R.color.turboBlue).copy(alpha = 0.5f),
                            colorResource(id = R.color.turboBlue)
                        )
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(5.dp))
                )
                .clickable {
                    coroutineScope.launch { modalBottomSheetState.show() }
                }
        ) {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "TurboWash".uppercase(Locale.ROOT),
                    modifier = Modifier
                        .padding(10.dp)
                        .alpha(0.5f),
                    style = TextStyle(
                        fontWeight = FontWeight.Black,
                        fontSize = 25.sp,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
            Text(
                text = maskedCardNumber(card.cardNumber), modifier = Modifier.padding(10.dp), style = TextStyle(
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Serif,
                    fontSize = 33.sp,
                    color = colorResource(id = R.color.fadedGray),
                    letterSpacing = 2.sp
                )
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = "Card Holder".uppercase(Locale.ROOT), style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.3.sp,
                            color = colorResource(
                                id = R.color.fadedGray
                            ).copy(alpha = 0.5f)
                        ), modifier = Modifier.padding(bottom = 3.dp)
                    )
                    Text(text = card.nameOnCard.uppercase(Locale.ROOT))
                }
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = "Valid Thru".uppercase(Locale.ROOT), style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.3.sp,
                            color = colorResource(
                                id = R.color.fadedGray
                            ).copy(alpha = 0.5f)
                        ), modifier = Modifier.padding(bottom = 3.dp)
                    )
                    Text(text = formatTimestamp(card.cardExpiry).uppercase(Locale.ROOT))
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(onTapUpdate: () -> Unit, onTapDelete: () -> Unit) {
    Row(modifier = Modifier.padding(16.dp)) {
        TextButton(
            onClick = onTapUpdate,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CustomPaddedIcon(icon = R.drawable.camera_add_filled)
                Spacer(Modifier.height(8.dp))
                Text("Open Camera")
            }
        }
        Spacer(Modifier.width(15.dp))
        TextButton(
            onClick = onTapDelete,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CustomPaddedIcon(icon = R.drawable.photo_1_outline)
                Spacer(Modifier.height(8.dp))
                Text("Browse Gallery")
            }
        }
    }
}