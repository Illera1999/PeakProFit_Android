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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.illera.peakprofit.R

@Immutable
data class PeakSpacing(
    val xxSmall: Dp,
    val xSmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val xLarge: Dp,
    val xxLarge: Dp
)

@Immutable
data class PeakComponentShapes(
    val field: RoundedCornerShape,
    val button: RoundedCornerShape,
    val card: RoundedCornerShape
)

@Immutable
data class PeakElevations(
    val field: Dp,
    val card: Dp
)

@Immutable
data class PeakSizes(
    val fieldMinHeight: Dp,
    val buttonMinHeight: Dp
)

private val zeroDp = Dp(0f)

private val LocalPeakColors = staticCompositionLocalOf {
    PeakExtendedColors(
        textPrimary = Color.Unspecified,
        textSecondary = Color.Unspecified,
        textInverted = Color.Unspecified,
        link = Color.Unspecified,
        divider = Color.Unspecified,
        canvas = Color.Unspecified,
        inputBackground = Color.Unspecified,
        inputBorder = Color.Unspecified,
        inputShadow = Color.Unspecified,
        disabledContainer = Color.Unspecified,
        disabledContent = Color.Unspecified,
        danger = Color.Unspecified
    )
}
private val LocalPeakSpacing = staticCompositionLocalOf {
    PeakSpacing(
        xxSmall = zeroDp,
        xSmall = zeroDp,
        small = zeroDp,
        medium = zeroDp,
        large = zeroDp,
        xLarge = zeroDp,
        xxLarge = zeroDp
    )
}
private val LocalPeakComponentShapes = staticCompositionLocalOf {
    PeakComponentShapes(
        field = RoundedCornerShape(zeroDp),
        button = RoundedCornerShape(zeroDp),
        card = RoundedCornerShape(zeroDp)
    )
}
private val LocalPeakElevations = staticCompositionLocalOf {
    PeakElevations(
        field = zeroDp,
        card = zeroDp
    )
}
private val LocalPeakSizes = staticCompositionLocalOf {
    PeakSizes(
        fieldMinHeight = zeroDp,
        buttonMinHeight = zeroDp
    )
}

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
private fun peakShapes() = Shapes(
    small = RoundedCornerShape(dimensionResource(R.dimen.peak_shape_material_small)),
    medium = RoundedCornerShape(dimensionResource(R.dimen.peak_shape_material_medium)),
    large = RoundedCornerShape(dimensionResource(R.dimen.peak_shape_material_large))
)

@Composable
private fun peakSpacing() = PeakSpacing(
    xxSmall = dimensionResource(R.dimen.peak_spacing_xx_small),
    xSmall = dimensionResource(R.dimen.peak_spacing_x_small),
    small = dimensionResource(R.dimen.peak_spacing_small),
    medium = dimensionResource(R.dimen.peak_spacing_medium),
    large = dimensionResource(R.dimen.peak_spacing_large),
    xLarge = dimensionResource(R.dimen.peak_spacing_x_large),
    xxLarge = dimensionResource(R.dimen.peak_spacing_xx_large)
)

@Composable
private fun peakComponentShapes() = PeakComponentShapes(
    field = RoundedCornerShape(dimensionResource(R.dimen.peak_shape_field)),
    button = RoundedCornerShape(dimensionResource(R.dimen.peak_shape_button)),
    card = RoundedCornerShape(dimensionResource(R.dimen.peak_shape_card))
)

@Composable
private fun peakElevations() = PeakElevations(
    field = dimensionResource(R.dimen.peak_elevation_field),
    card = dimensionResource(R.dimen.peak_elevation_card)
)

@Composable
private fun peakSizes() = PeakSizes(
    fieldMinHeight = dimensionResource(R.dimen.peak_size_field_min_height),
    buttonMinHeight = dimensionResource(R.dimen.peak_size_button_min_height)
)

@Composable
private fun peakExtendedColors(darkTheme: Boolean): PeakExtendedColors {
    val textPrimary = colorResource(
        if (darkTheme) R.color.peak_text_primary_dark else R.color.peak_text_primary_light
    )
    val textSecondary = colorResource(
        if (darkTheme) R.color.peak_text_secondary_dark else R.color.peak_text_secondary_light
    )

    return PeakExtendedColors(
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textInverted = colorResource(R.color.white),
        link = colorResource(if (darkTheme) R.color.peak_link_dark else R.color.peak_link_light),
        divider = colorResource(
            if (darkTheme) R.color.peak_divider_dark else R.color.peak_divider_light
        ),
        canvas = colorResource(
            if (darkTheme) R.color.peak_canvas_dark else R.color.peak_canvas_light
        ),
        inputBackground = colorResource(
            if (darkTheme) {
                R.color.peak_input_background_dark
            } else {
                R.color.peak_input_background_light
            }
        ),
        inputBorder = colorResource(
            if (darkTheme) R.color.peak_input_border_dark else R.color.peak_input_border_light
        ),
        inputShadow = colorResource(
            if (darkTheme) R.color.peak_field_shadow_dark else R.color.peak_field_shadow_light
        ),
        disabledContainer = colorResource(
            if (darkTheme) {
                R.color.peak_disabled_container_dark
            } else {
                R.color.peak_disabled_container_light
            }
        ),
        disabledContent = colorResource(
            if (darkTheme) {
                R.color.peak_disabled_content_dark
            } else {
                R.color.peak_disabled_content_light
            }
        ),
        danger = colorResource(if (darkTheme) R.color.peak_error_dark else R.color.peak_error_light)
    )
}

@Composable
private fun peakColorScheme(
    darkTheme: Boolean,
    extendedColors: PeakExtendedColors
) = if (darkTheme) {
    darkColorScheme(
        primary = colorResource(R.color.peak_accent_dark),
        onPrimary = extendedColors.textInverted,
        secondary = colorResource(R.color.peak_link_dark),
        onSecondary = extendedColors.textPrimary,
        background = colorResource(R.color.peak_background_dark),
        onBackground = extendedColors.textPrimary,
        surface = colorResource(R.color.peak_surface_dark),
        onSurface = extendedColors.textPrimary,
        surfaceVariant = colorResource(R.color.peak_canvas_dark),
        onSurfaceVariant = extendedColors.textSecondary,
        outline = colorResource(R.color.peak_input_border_dark),
        error = colorResource(R.color.peak_error_dark),
        onError = colorResource(R.color.black)
    )
} else {
    lightColorScheme(
        primary = colorResource(R.color.peak_accent_light),
        onPrimary = extendedColors.textInverted,
        secondary = colorResource(R.color.peak_link_light),
        onSecondary = extendedColors.textInverted,
        background = colorResource(R.color.peak_background_light),
        onBackground = extendedColors.textPrimary,
        surface = colorResource(R.color.peak_surface_light),
        onSurface = extendedColors.textPrimary,
        surfaceVariant = colorResource(R.color.peak_canvas_light),
        onSurfaceVariant = extendedColors.textSecondary,
        outline = colorResource(R.color.peak_input_border_light),
        error = colorResource(R.color.peak_error_light),
        onError = extendedColors.textInverted
    )
}

@Composable
fun PeakProFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val extendedColors = peakExtendedColors(darkTheme)
    val spacing = peakSpacing()
    val componentShapes = peakComponentShapes()
    val elevations = peakElevations()
    val sizes = peakSizes()
    val colorScheme = peakColorScheme(
        darkTheme = darkTheme,
        extendedColors = extendedColors
    )

    CompositionLocalProvider(
        LocalPeakColors provides extendedColors,
        LocalPeakSpacing provides spacing,
        LocalPeakComponentShapes provides componentShapes,
        LocalPeakElevations provides elevations,
        LocalPeakSizes provides sizes
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = peakShapes(),
            content = content
        )
    }
}
