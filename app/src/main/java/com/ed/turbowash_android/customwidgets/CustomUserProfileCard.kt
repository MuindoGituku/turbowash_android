package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme

@Composable
fun CustomUserProfileCard (
    onProfileCardClick: () -> Unit,
    cardPaddingValues: PaddingValues = PaddingValues(10.dp),
    innerPaddingValues: PaddingValues = PaddingValues(10.dp),
    cornerRadius: Dp = 7.dp,
    imagePainter: Painter = painterResource(id = R.drawable.user_png),
    imageWidth: Dp = 70.dp,
    imageHeight: Dp = 70.dp,
    fullNames: String,
    fullNamesStyle: TextStyle = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Black,
    ),
    emailAddress: String,
    emailAddressStyle: TextStyle = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.W300
    ),
    phoneNumber: String,
    phoneNumberStyle: TextStyle = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.W300
    ),
    cardBackgroundColor: Color = colorResource(id = R.color.fadedGray)
) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        ),
        shape = RoundedCornerShape(corner = CornerSize(cornerRadius)),
        modifier = Modifier
            .clickable { onProfileCardClick() }
            .padding(cardPaddingValues)
            .fillMaxWidth()
            .background(
                color = cardBackgroundColor,
                shape = RoundedCornerShape(corner = CornerSize(cornerRadius)),
            )
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(innerPaddingValues)
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "User profile image",
                modifier = Modifier
                    .size(width = imageWidth, height = imageHeight)
            )
            Column(
                modifier = Modifier.padding(start = 10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = fullNames,
                    style = fullNamesStyle,
                    maxLines = 1,
                    textAlign = TextAlign.Start
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 7.dp)
                            .size(5.dp)
                            .background(
                                color = Color.Black,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = emailAddress,
                        style = emailAddressStyle
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                            .size(5.dp)
                            .background(
                                color = Color.Black,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = phoneNumber,
                        style = phoneNumberStyle
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomUserProfileCardPreview(){
    TurboWash_AndroidTheme {
        CustomUserProfileCard (
            onProfileCardClick = { },
            fullNames = "Muindo N. Gituku",
            emailAddress = "muindogituku@gmail.com",
            phoneNumber = "(437) 661-2182",
        )
    }
}