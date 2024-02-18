package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ed.turbowash_android.models.OnboardingStep
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme

@Composable
fun OnboardingScreenView(page: OnboardingStep) {
    Column(
        modifier = Modifier.padding(PaddingValues(horizontal = 20.dp)),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageURL),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Text(
            text = page.name,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp)
        )

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview3() {
    TurboWash_AndroidTheme {
        OnboardingScreenView(OnboardingStep.samplePage)
    }
}