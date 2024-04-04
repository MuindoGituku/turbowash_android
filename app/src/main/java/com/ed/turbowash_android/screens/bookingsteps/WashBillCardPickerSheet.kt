package com.ed.turbowash_android.screens.bookingsteps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.models.PaymentCard

@Composable
fun WashBillCardPickerSheet (cardsList: MutableList<PaymentCard>, onCardConfirmed: () -> Unit) {
    Column (
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
    }
}