package com.ngoctientnt.template.feature.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ngoctientnt.template.app.navigation.HomeRoute
import com.ngoctientnt.template.app.navigation.LocalAppNavigator
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    val navigator = LocalAppNavigator.current

    LaunchedEffect(Unit) {
        delay(1_500)
        // TODO: check auth state — replaceAll(LoginRoute) if not logged in
        navigator.replaceAll(HomeRoute)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
        Text(
            text = "Loading...",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
