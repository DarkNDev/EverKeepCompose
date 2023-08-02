package com.darkndev.everkeepcompose.utils

import android.os.Bundle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavHostController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val destination = graph.findNode(route = route)
    if (destination != null)
        navigate(destination.id, args, navOptions, navigatorExtras)
}