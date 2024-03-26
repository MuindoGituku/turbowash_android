package com.ed.turbowash_android.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ed.turbowash_android.R
import com.ed.turbowash_android.customwidgets.MaxWidthButton
import com.ed.turbowash_android.viewmodels.UserAuthenticationViewModel

@Composable
fun InvalidCredentialsActivity(viewModel: UserAuthenticationViewModel, onLoggedOutSuccessfully: () -> Unit){
    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.turbowash_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        )

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "You have used an already registered email address. Please try again!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        MaxWidthButton(
            buttonText = "Try Another Email Address",
            buttonAction = {  },
            backgroundColor = colorResource(id = R.color.turboBlue),
            customImageName = R.drawable.mail_filled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = buildAnnotatedString {
                append("By authenticating using either of the above buttons, you agree to the ")
                withStyle(style = TextStyle(fontWeight = FontWeight.Bold, color = colorResource(id = R.color.turboBlue), textDecoration = TextDecoration.Underline).toSpanStyle()) {
                    append("Privacy Policy")
                }
                append(" and the ")
                withStyle(style = TextStyle(fontWeight = FontWeight.Bold, color = colorResource(id = R.color.turboBlue), textDecoration = TextDecoration.Underline).toSpanStyle()) {
                    append("Terms of Service")
                }
                append(" for the TurboWash mobile application.")
            },
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 12.sp, lineHeight = 20.sp)
        )
        Spacer(modifier = Modifier.padding(bottom = 20.dp))
    }
}