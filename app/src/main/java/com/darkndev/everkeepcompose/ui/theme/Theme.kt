package com.darkndev.everkeepcompose.ui.theme

import android.app.Activity
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.darkndev.everkeepcompose.ui.note.NoteViewModel
import com.darkndev.everkeepcompose.utils.getColor

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
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
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
    viewModel: NoteViewModel, //container, content
    content: @Composable () -> Unit
) {
    val colorsRef = viewModel.colorRef
    val colors = getColor(colorsRef)
    val containerColor = remember {
        Animatable(colors.first)
    }
    val contentColor = remember {
        Animatable(colors.second)
    }
    LaunchedEffect(key1 = colorsRef) {
        containerColor.animateTo(
            targetValue = getColor(colorsRef).first,
            animationSpec = tween(
                durationMillis = 1000
            )
        )
    }
    LaunchedEffect(key1 = colorsRef){
        contentColor.animateTo(
            targetValue = getColor(colorsRef).second,
            animationSpec = tween(
                durationMillis = 1000
            )
        )
    }
    val colorScheme = when {
        darkTheme -> darkColorScheme(
            background = contentColor.value,
            surface = contentColor.value,
            onBackground = containerColor.value,
            onSurface = containerColor.value,
            onSurfaceVariant = containerColor.value
        )

        else -> lightColorScheme(
            background = containerColor.value,
            surface = containerColor.value,
            onBackground = contentColor.value,
            onSurface = contentColor.value,
            onSurfaceVariant = contentColor.value
        )
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
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