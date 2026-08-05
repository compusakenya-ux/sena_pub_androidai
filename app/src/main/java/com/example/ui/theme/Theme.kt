package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SenaColorScheme = darkColorScheme(
    primary = SenaPeach,
    onPrimary = SenaBackground,
    primaryContainer = SenaOrangeGlow,
    onPrimaryContainer = SenaPeach,
    secondary = SenaElectricCyan,
    onSecondary = SenaBackground,
    secondaryContainer = SenaElectricGlow,
    onSecondaryContainer = SenaElectricCyan,
    background = SenaBackground,
    onBackground = SenaTextPrimary,
    surface = SenaSurface,
    onSurface = SenaTextPrimary,
    surfaceVariant = SenaSurfaceVariant,
    onSurfaceVariant = SenaTextSecondary,
    outline = SenaBorder
)

@Composable
fun SenaTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SenaBackground.toArgb()
            window.navigationBarColor = SenaBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = SenaColorScheme,
        typography = Typography,
        content = content
    )
}

