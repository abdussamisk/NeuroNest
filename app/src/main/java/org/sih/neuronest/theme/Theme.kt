package org.sih.neuronest.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryTealLight,
    onPrimary = Color.Black,
    primaryContainer = PrimaryTealDark,
    onPrimaryContainer = PatientTextPrimary,
    secondary = SecondaryWarmGold,
    onSecondary = Color.Black,
    background = PatientBackgroundDark,
    onBackground = PatientTextPrimary,
    surface = PatientSurfaceDark,
    onSurface = PatientTextPrimary,
    surfaceVariant = PatientCardDark,
    onSurfaceVariant = PatientTextSecondary,
    error = GameDangerCoral
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCCFBF1),
    onPrimaryContainer = Color(0xFF0F5157),
    secondary = SecondaryWarmGold,
    onSecondary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    error = GameDangerCoral
)

@Composable
fun NeuroNestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PatientTypography,
        content = content
    )
}
