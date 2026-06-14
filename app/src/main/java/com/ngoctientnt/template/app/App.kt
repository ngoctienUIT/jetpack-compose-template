package com.ngoctientnt.template.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ngoctientnt.template.app.navigation.AppBackStack
import com.ngoctientnt.template.app.navigation.AppNavHost
import com.ngoctientnt.template.app.navigation.AppNavigator
import com.ngoctientnt.template.core.appinfo.AppInfoManager
import com.ngoctientnt.template.core.appinfo.LocalAppInfo
import com.ngoctientnt.template.core.appinfo.LocalAppInfoManager
import com.ngoctientnt.template.feature.connectivity.ConnectivityViewModel
import com.ngoctientnt.template.ui.component.NoInternetDialog

@Composable
fun App(
    appBackStack: AppBackStack,
    appNavigator: AppNavigator,
    appInfoManager: AppInfoManager,
) {
    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
    val networkStatus by connectivityViewModel.networkStatus.collectAsStateWithLifecycle()
    val appInfo by appInfoManager.appInfo.collectAsStateWithLifecycle()

    NoInternetDialog(
        networkStatus = networkStatus,
        onRetry = connectivityViewModel::refreshNetworkStatus,
    )

    CompositionLocalProvider(
        LocalAppInfo provides appInfo,
        LocalAppInfoManager provides appInfoManager,
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
}
