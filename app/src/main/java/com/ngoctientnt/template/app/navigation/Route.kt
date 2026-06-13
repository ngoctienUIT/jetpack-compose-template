package com.ngoctientnt.template.app.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

@Serializable
data object LoginRoute

@Serializable
data object HomeRoute

@Serializable
data class DetailRoute(
    val id: String
)

@Serializable
data object ProfileRoute