package com.darkndev.everkeepcompose.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun EverKeepComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun NoteComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: Pair<Color, Color>, //container, content
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme(
            background = colors.second,
            surface = colors.second,
            onBackground = colors.first,
            onSurface = colors.first,
            onSurfaceVariant = colors.first
        )

        else -> lightColorScheme(
            background = colors.first,
            surface = colors.first,
            onBackground = colors.second,
            onSurface = colors.second,
            onSurfaceVariant = colors.second
        )
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(color = colorScheme.onSurface),
            titleLarge = MaterialTheme.typography.titleLarge.copy(color = colorScheme.onSurface),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.onSurface)
        ),
        content = content
    )
}

@Composable
fun NoteCardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: Pair<Color, Color>, //container, content
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme(
            background = colors.second,
            surface = colors.second,
            onBackground = colors.first,
            onSurface = colors.first,
        )

        else -> lightColorScheme(
            background = colors.first,
            surface = colors.first,
            onBackground = colors.second,
            onSurface = colors.second,
        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}