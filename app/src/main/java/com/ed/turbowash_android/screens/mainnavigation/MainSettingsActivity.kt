package com.ed.turbowash_android.screens.mainnavigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ed.turbowash_android.ui.theme.TurboWash_AndroidTheme


@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    TurboWash_AndroidTheme {
        Greeting4("Android")
    }
}