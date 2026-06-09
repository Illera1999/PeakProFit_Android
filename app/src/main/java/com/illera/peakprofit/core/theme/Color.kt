package com.illera.peakprofit.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val PeakAccent = Color(0xFF51E05E)
val PeakAccentDark = Color(0xFF44C650)
val PeakLink = Color(0xFF4E82FF)
val PeakLinkDark = Color(0xFF8FB0FF)
val PeakTextPrimaryLight = Color(0xFF161616)
val PeakTextPrimaryDark = Color(0xFFF5F7FB)
val PeakTextSecondaryLight = Color(0xFF68738D)
val PeakTextSecondaryDark = Color(0xFFABB7D3)
val PeakDividerLight = Color(0xFFD8DECF)
val PeakDividerDark = Color(0xFF252B38)
val PeakInputBackgroundLight = Color(0xFFF8F8F4)
val PeakInputBackgroundDark = Color(0xFF171C25)
val PeakInputBorderLight = Color(0xFFD4DBCC)
val PeakInputBorderDark = Color(0xFF333C4E)
val PeakCanvasLight = Color(0xFFF1F4EC)
val PeakCanvasDark = Color(0xFF0E1218)
val PeakErrorLight = Color(0xFFC33B3B)
val PeakErrorDark = Color(0xFFFF8A80)
val PeakDisabledContainerLight = Color(0xFFE5EADF)
val PeakDisabledContainerDark = Color(0xFF273141)
val PeakDisabledContentLight = Color(0xFF95A1B8)
val PeakDisabledContentDark = Color(0xFF61708C)
val PeakFieldShadowLight = Color(0x12243B53)
val PeakFieldShadowDark = Color(0x40000000)

@Immutable
data class PeakExtendedColors(
    val textPrimary: Color,
    val textSecondary: Color,
    val textInverted: Color,
    val link: Color,
    val divider: Color,
    val canvas: Color,
    val inputBackground: Color,
    val inputBorder: Color,
    val inputShadow: Color,
    val disabledContainer: Color,
    val disabledContent: Color,
    val danger: Color
)

internal val LightExtendedColors = PeakExtendedColors(
    textPrimary = PeakTextPrimaryLight,
    textSecondary = PeakTextSecondaryLight,
    textInverted = Color.White,
    link = PeakLink,
    divider = PeakDividerLight,
    canvas = PeakCanvasLight,
    inputBackground = PeakInputBackgroundLight,
    inputBorder = PeakInputBorderLight,
    inputShadow = PeakFieldShadowLight,
    disabledContainer = PeakDisabledContainerLight,
    disabledContent = PeakDisabledContentLight,
    danger = PeakErrorLight
)

internal val DarkExtendedColors = PeakExtendedColors(
    textPrimary = PeakTextPrimaryDark,
    textSecondary = PeakTextSecondaryDark,
    textInverted = Color.White,
    link = PeakLinkDark,
    divider = PeakDividerDark,
    canvas = PeakCanvasDark,
    inputBackground = PeakInputBackgroundDark,
    inputBorder = PeakInputBorderDark,
    inputShadow = PeakFieldShadowDark,
    disabledContainer = PeakDisabledContainerDark,
    disabledContent = PeakDisabledContentDark,
    danger = PeakErrorDark
)
