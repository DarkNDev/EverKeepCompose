package com.darkndev.everkeepcompose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.ui.home.HomeScreen
import com.darkndev.everkeepcompose.ui.note.NoteScreen
import com.darkndev.everkeepcompose.ui.theme.EverKeepComposeTheme
import com.darkndev.everkeepcompose.ui.theme.NoteComposeTheme
import com.darkndev.everkeepcompose.utils.Routes
import com.darkndev.everkeepcompose.utils.getColor
import com.darkndev.everkeepcompose.utils.navigate
import com.darkndev.everkeepcompose.utils.parcelable

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen.route,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(
            route = Routes.HomeScreen.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            }
        ) {
            EverKeepComposeTheme {
                HomeScreen { note ->
                    navController.navigate(
                        route = Routes.NoteScreen.route,
                        args = bundleOf("note" to note)
                    )
                }
            }
        }
        composable(
            route = Routes.NoteScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            val note = it.arguments?.parcelable<Note>("note")
            NoteComposeTheme(colors = getColor(note?.color)) {
                NoteScreen {
                    navController.popBackStack()
                }
            }
        }
    }
}