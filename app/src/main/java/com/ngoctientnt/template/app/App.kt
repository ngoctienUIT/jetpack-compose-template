package com.ngoctientnt.template.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.ngoctientnt.template.ui.component.toast.AppToastHost
import com.ngoctientnt.template.ui.component.toast.AppToastPosition
import com.ngoctientnt.template.ui.component.toast.ProvideAppToastController
import com.ngoctientnt.template.ui.component.toast.rememberAppToastController

@Composable
fun App(
    appBackStack: AppBackStack,
    appNavigator: AppNavigator,
    appInfoManager: AppInfoManager,
) {
    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
    val networkStatus by connectivityViewModel.networkStatus.collectAsStateWithLifecycle()
    val appInfo by appInfoManager.appInfo.collectAsStateWithLifecycle()
    val toastController = rememberAppToastController()

    NoInternetDialog(
        networkStatus = networkStatus,
        onRetry = connectivityViewModel::refreshNetworkStatus,
    )

    ProvideAppToastController(controller = toastController) {
        CompositionLocalProvider(
            LocalAppInfo provides appInfo,
            LocalAppInfoManager provides appInfoManager,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavHost(
                        appBackStack = appBackStack,
                        appNavigator = appNavigator,
                    )
                }
                val toastPosition = toastController.currentToast?.let(toastController::resolvePosition)
                    ?: AppToastPosition.Bottom
                AppToastHost(
                    controller = toastController,
                    modifier = Modifier
                        .align(
                            when (toastPosition) {
                                AppToastPosition.Top -> Alignment.TopCenter
                                AppToastPosition.Bottom -> Alignment.BottomCenter
                            },
                        )
                        .fillMaxWidth(),
                )
            }
        }
    }
}
