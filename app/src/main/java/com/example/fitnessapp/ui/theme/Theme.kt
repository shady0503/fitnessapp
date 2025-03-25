package com.example.fitnessapp.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightFancyColorScheme = lightColorScheme(
    primary = FancyPurple,
    onPrimary = Color.White,
    secondary = FancyBlue,
    onSecondary = Color.White,
    background = FancyBackground,
    onBackground = FancyOnBackground,
    surface = FancySurface,
    onSurface = FancyOnSurface
)

private val DarkFancyColorScheme = darkColorScheme(
    primary = FancyPurple,
    onPrimary = Color.White,
    secondary = FancyBlue,
    onSecondary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.DarkGray,
    onSurface = Color.White
)

@Composable
fun FitnessAppTheme(
    darkTheme: Boolean = false,
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
