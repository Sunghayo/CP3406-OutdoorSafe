package au.edu.jcu.assessment.utilityapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = PaleMint,
    onPrimaryContainer = DeepGreen,
    secondaryContainer = Color(0xFFE3F0E9),
    background = QuietBackground,
    surface = QuietBackground
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9CD6BC),
    primaryContainer = DeepGreen,
    secondaryContainer = Color(0xFF294C42)
)

@Composable
fun OutdoorSafeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
