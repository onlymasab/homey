package com.paandaaa.homey.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.paandaaa.homey.android.ui.theme.AppShapes
import com.paandaaa.homey.android.ui.theme.BackgroundDark
import com.paandaaa.homey.android.ui.theme.BackgroundLight
import com.paandaaa.homey.android.ui.theme.Primary
import com.paandaaa.homey.android.ui.theme.Secondary

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = BackgroundLight,
    onPrimary = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = BackgroundDark,
    onPrimary = Color.Black
)

@Composable
fun HomeyTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}