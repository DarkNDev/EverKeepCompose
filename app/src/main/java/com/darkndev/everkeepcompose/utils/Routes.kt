package com.darkndev.everkeepcompose.utils

sealed class Routes(val route: String) {
    data object HomeScreen : Routes("home")
    data object NoteScreen : Routes("note")
    data object LabelScreen : Routes("label")
    data object ArchivedScreen : Routes("archived")
}
