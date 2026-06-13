package com.ngoctientnt.template.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ngoctientnt.template.app.navigation.AppBackStack
import com.ngoctientnt.template.app.navigation.AppNavHost
import com.ngoctientnt.template.app.navigation.AppNavigator
import com.ngoctientnt.template.feature.connectivity.ConnectivityViewModel
import com.ngoctientnt.template.ui.component.NoInternetDialog

@Composable
fun App(
    appBackStack: AppBackStack,
    appNavigator: AppNavigator,
) {
    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
    val networkStatus by connectivityViewModel.networkStatus.collectAsStateWithLifecycle()

    NoInternetDialog(
        networkStatus = networkStatus,
        onRetry = connectivityViewModel::refreshNetworkStatus,
    )

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
