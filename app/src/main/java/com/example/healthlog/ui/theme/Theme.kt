package com.example.healthlog.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LavenderAccent,
    secondary = LavenderPrimary,
    tertiary = LavenderLight,
    background = Black,
    surface = Gray900,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray500,
    outline = Gray700,
    outlineVariant = Gray900,
    primaryContainer = LavenderDark,
    onPrimaryContainer = White
)

private val LightColorScheme = lightColorScheme(
    primary = LavenderPrimary,
    secondary = LavenderAccent,
    tertiary = LavenderLight,
    background = Color(0xFFF8FAFC),
    surface = White,
    onPrimary = White,
    onSecondary = Gray900,
    onBackground = Black,
    onSurface = Black,
    primaryContainer = LavenderLight,
    onPrimaryContainer = LavenderDark,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,
    outline = Gray500,
    outlineVariant = Gray100
)

@Composable
fun HealthLogTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
