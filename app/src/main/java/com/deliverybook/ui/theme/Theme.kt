package com.deliverybook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun DeliveryBookTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Preview(name = "Theme light", showBackground = true)
@Composable
private fun DeliveryBookThemeLightPreview() {
    DeliveryBookTheme(useDarkTheme = false) {
        Surface {
            Text("Delivery Book Theme")
        }
    }
}

@Preview(name = "Theme dark", showBackground = true)
@Composable
private fun DeliveryBookThemeDarkPreview() {
    DeliveryBookTheme(useDarkTheme = true) {
        Surface {
            Text("Delivery Book Theme")
        }
    }
}

