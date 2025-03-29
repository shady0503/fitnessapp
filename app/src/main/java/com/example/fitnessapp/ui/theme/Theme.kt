package com.example.fitnessapp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightFancyColorScheme = lightColorScheme(
    primary = FancyPurple,
    onPrimary = FancyOnPrimary,
    secondary = FancyBlue,
    onSecondary = FancyOnSecondary,
    tertiary = FancyOrange,
    background = FancyBackground,
    onBackground = FancyOnBackground,
    surface = FancySurface,
    onSurface = FancyOnSurface,
    error = FancyErrorRed,
    surfaceVariant = Color.LightGray.copy(alpha = 0.5f)
)

private val DarkFancyColorScheme = darkColorScheme(
    primary = FancyPurple,
    onPrimary = Color.White,
    secondary = FancyBlue,
    onSecondary = Color.White,
    tertiary = FancyOrange,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF252525),
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    surfaceVariant = Color.DarkGray.copy(alpha = 0.5f)
)

@Composable
fun FitnessAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkFancyColorScheme else LightFancyColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FancyTypography,
        shapes = FancyShapes,
        content = content
    )
}