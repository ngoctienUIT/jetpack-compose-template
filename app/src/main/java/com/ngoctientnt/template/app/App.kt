package com.ngoctientnt.template.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ngoctientnt.template.app.navigation.AppBackStack
import com.ngoctientnt.template.app.navigation.AppNavHost
import com.ngoctientnt.template.app.navigation.AppNavigator

@Composable
fun App(
    appBackStack: AppBackStack,
    appNavigator: AppNavigator,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        AppNavHost(
            appBackStack = appBackStack,
            appNavigator = appNavigator,
        )
    }
}
