package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.models.PaymentCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomSinglePaymentCardTile(
    card: PaymentCard,
    onTapUpdate: () -> Void,
    onTapDelete: () -> Void,
    outerPaddingValues: PaddingValues = PaddingValues(10.dp),
    innerPaddingValues: PaddingValues = PaddingValues(10.dp)
){
    val modalBottomSheetState =
        androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            BottomSheetContent (
                onTapDelete = { onTapDelete() },
                onTapUpdate = { onTapUpdate() }
            )
        }
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(outerPaddingValues)
                .height(150.dp)
                .background(color = colorResource(id = R.color.turboBlue))
                .clickable {
                    coroutineScope.launch { modalBottomSheetState.show() }
                }
        ) {
            Text(text = "TurboWash")
            Text(text = card.cardNumber)
            Row {
                Column {
                    Text(text = "Card Holder")
                    Text(text = card.nameOnCard)
                }
                Column {
                    Text(text = "Valid Thru")
                    Text(text = card.cardExpiry.toDate().toString())
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