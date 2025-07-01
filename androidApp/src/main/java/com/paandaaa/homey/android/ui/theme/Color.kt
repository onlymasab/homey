package com.paandaaa.homey.android.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.ColorScheme

val KoreanRed = Color(0xFFE74C3C)
val SoftPink = Color(0xFFFFE5E5)
val DarkCharcoal = Color(0xFF2C3E50)
val White = Color(0xFFFFFFFF)
val LightGray = Color(0xFFF5F5F5)



val LightColorScheme = lightColorScheme(
    primary = KoreanRed,
    onPrimary = White,
    secondary = SoftPink,
    onSecondary = DarkCharcoal,
    background = White,
    onBackground = DarkCharcoal,
    surface = LightGray,
    onSurface = DarkCharcoal,
)

val DarkColorScheme = darkColorScheme(
    primary = KoreanRed,
    onPrimary = White,
    secondary = SoftPink,
    onSecondary = White,
    background = DarkCharcoal,
    onBackground = White,
    surface = Color(0xFF1C1C1C),
    onSurface = White,
)