package fr.gir.gameedr.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GameEdrColorScheme = darkColorScheme(
    primary = Brass,
    onPrimary = NightCoal,
    secondary = TacticalOlive,
    onSecondary = SandLight,
    tertiary = Sand,
    background = NightCoal,
    onBackground = SandLight,
    surface = DarkSoil,
    onSurface = SandLight,
    surfaceVariant = FieldBrown,
    onSurfaceVariant = Sand,
    outline = BorderMuted,
    error = SignalRed,
    onError = SandLight
)

@Composable
fun GameEDRTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GameEdrColorScheme,
        typography = GameEdrTypography,
        shapes = GameEdrShapes,
        content = content
    )
}
