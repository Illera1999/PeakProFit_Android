package com.illera.peakprofit.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val ColorLightBackground = Color(0xFFF7F8F2)
private val ColorLightSurface = Color(0xFFFBFBF7)
private val ColorDarkBackground = Color(0xFF080B10)
private val ColorDarkSurface = Color(0xFF11161D)

private val LightColors = lightColorScheme(
    primary = PeakAccent,
    onPrimary = LightExtendedColors.textInverted,
    secondary = PeakLink,
    onSecondary = LightExtendedColors.textInverted,
    background = ColorLightBackground,
    onBackground = LightExtendedColors.textPrimary,
    surface = ColorLightSurface,
    onSurface = LightExtendedColors.textPrimary,
    surfaceVariant = PeakCanvasLight,
    onSurfaceVariant = LightExtendedColors.textSecondary,
    outline = PeakInputBorderLight,
    error = PeakErrorLight,
    onError = LightExtendedColors.textInverted
)

private val DarkColors = darkColorScheme(
    primary = PeakAccentDark,
    onPrimary = DarkExtendedColors.textInverted,
    secondary = PeakLinkDark,
    onSecondary = DarkExtendedColors.textPrimary,
    background = ColorDarkBackground,
    onBackground = DarkExtendedColors.textPrimary,
    surface = ColorDarkSurface,
    onSurface = DarkExtendedColors.textPrimary,
    surfaceVariant = PeakCanvasDark,
    onSurfaceVariant = DarkExtendedColors.textSecondary,
    outline = PeakInputBorderDark,
    error = PeakErrorDark,
    onError = Color.Black
)

private val PeakShapes = Shapes(
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp)
)

@Immutable
data class PeakSpacing(
    val xxSmall: Dp = 4.dp,
    val xSmall: Dp = 8.dp,
    val small: Dp = 12.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val xLarge: Dp = 32.dp,
    val xxLarge: Dp = 48.dp
)

@Immutable
data class PeakComponentShapes(
    val field: RoundedCornerShape = RoundedCornerShape(18.dp),
    val button: RoundedCornerShape = RoundedCornerShape(16.dp),
    val card: RoundedCornerShape = RoundedCornerShape(24.dp)
)

@Immutable
data class PeakElevations(
    val field: Dp = 6.dp,
    val card: Dp = 2.dp
)

@Immutable
data class PeakSizes(
    val fieldMinHeight: Dp = 52.dp,
    val buttonMinHeight: Dp = 54.dp
)

private val LocalPeakColors = staticCompositionLocalOf { LightExtendedColors }
private val LocalPeakSpacing = staticCompositionLocalOf { PeakSpacing() }
private val LocalPeakComponentShapes = staticCompositionLocalOf { PeakComponentShapes() }
private val LocalPeakElevations = staticCompositionLocalOf { PeakElevations() }
private val LocalPeakSizes = staticCompositionLocalOf { PeakSizes() }

object PeakTheme {
    val colors: PeakExtendedColors
        @Composable get() = LocalPeakColors.current

    val spacing: PeakSpacing
        @Composable get() = LocalPeakSpacing.current

    val shapes: PeakComponentShapes
        @Composable get() = LocalPeakComponentShapes.current

    val elevations: PeakElevations
        @Composable get() = LocalPeakElevations.current

    val sizes: PeakSizes
        @Composable get() = LocalPeakSizes.current
}

@Composable
fun PeakProFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        LocalPeakColors provides extendedColors,
        LocalPeakSpacing provides PeakSpacing(),
        LocalPeakComponentShapes provides PeakComponentShapes(),
        LocalPeakElevations provides PeakElevations(),
        LocalPeakSizes provides PeakSizes()
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = Typography,
            shapes = PeakShapes,
            content = content
        )
    }
}
