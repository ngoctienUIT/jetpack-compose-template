package com.ngoctientnt.template.app.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

@Serializable
data object LoginRoute

@Serializable
data class MainRoute(
    val tab: String = BottomNavTab.HOME.name,
)

@Serializable
data class DetailRoute(
    val id: String,
)

@Serializable
data object FavoriteRoute