package com.ngoctientnt.template.app.navigation

import androidx.compose.runtime.compositionLocalOf

val LocalAppNavigator = compositionLocalOf<AppNavigator> {
    error("AppNavigator is not provided. Wrap content with App() root composable.")
}
