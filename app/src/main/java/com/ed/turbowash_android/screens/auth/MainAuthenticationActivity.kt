package com.ed.turbowash_android.screens.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


@Composable
fun AuthActivityScreenView(viewModel: UserAuthenticationViewModel, onAuthCompletedSuccessfully: () -> Unit) {
    val context = LocalContext.current
    val authenticated by viewModel.authenticationState.observeAsState(initial = false)

    LaunchedEffect(authenticated) {
        if (authenticated) {
            onAuthCompletedSuccessfully()
        }
    }

    // Setup Google SignIn Client
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    // Activity Result Launcher for Google Sign-In
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)
                viewModel.authenticateWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google sign in failed", e)
                // Optionally, update UI to reflect sign in failure here
            }
        }
    }

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
            text = "On Demand Car Wash\nDelivered At Your Doorstep",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        MaxWidthButton(
            buttonText = "Continue With Apple",
            buttonAction = {  },
            backgroundColor = colorResource(id = R.color.fadedGray),
            customImageName = R.drawable.apple_logo,
            cornerRadius = 10,
        )

        Spacer(modifier = Modifier.height(10.dp))
        MaxWidthButton(
            buttonText = "Continue With Google",
            buttonAction = {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            },
            backgroundColor = colorResource(id = R.color.fadedGray),
            customImageName = R.drawable.google_logo,
            cornerRadius = 10,
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)) {
            Divider(
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "OR",
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            Divider(
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        MaxWidthButton(
            buttonText = "Continue With Email",
            buttonAction = {  },
            backgroundColor = colorResource(id = R.color.fadedGray),
            customImageName = R.drawable.mail_filled,
            cornerRadius = 10,
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
        Spacer(modifier = Modifier.weight(1f))
    }
}