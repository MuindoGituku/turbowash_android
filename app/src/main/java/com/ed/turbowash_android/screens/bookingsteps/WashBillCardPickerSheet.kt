package com.ed.turbowash_android.screens.bookingsteps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.CustomCardRadioButton
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.models.PaymentCard

@Composable
fun WashBillCardPickerSheet(
    cardsList: MutableList<PaymentCard>,
    onCardConfirmed: (PaymentCard) -> Unit,
    onClickAddNewCard: () -> Unit,
    currentSelectedCard: PaymentCard?
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.9f)
    ) {
        Text(
            text = "Select Billing Card",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.W900,
                fontSize = 30.sp
            ),
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 10.dp, top = 20.dp)
        )
        LazyColumn() {
            items(cardsList) {
                CustomCardRadioButton(
                    card = it,
                    onTapCard = onCardConfirmed,
                    cardSelected = currentSelectedCard == it
                )
            }
        }
        MaxWidthButton(
            buttonText = "Add New Card",
            buttonAction = { onClickAddNewCard() },
            customTextColor = Color.White,
            customImageName = R.drawable.add_card_filled,
            customImageColor = colorResource(id = R.color.fadedGray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .background(
                    color = colorResource(id = R.color.turboBlue),
                    shape = RoundedCornerShape(corner = CornerSize(5.dp))
                )
                .border(
                    2.dp,
                    color = colorResource(id = R.color.turboBlue),
                    shape = RoundedCornerShape(corner = CornerSize(5.dp))
                ),
        )
    }
}