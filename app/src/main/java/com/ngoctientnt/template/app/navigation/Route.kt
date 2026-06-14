package com.ngoctientnt.template.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute : NavKey

@Serializable
data object LoginRoute : NavKey

@Serializable
data class MainRoute(
    val tab: String = BottomNavTab.HOME.name,
) : NavKey

@Serializable
data class DetailRoute(
    val id: String,
) : NavKey

@Serializable
data object FavoriteRoute : NavKey
