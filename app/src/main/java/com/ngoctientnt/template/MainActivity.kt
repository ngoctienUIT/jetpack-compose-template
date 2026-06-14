package com.ngoctientnt.template

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ngoctientnt.template.app.App
import com.ngoctientnt.template.app.navigation.AppBackStack
import com.ngoctientnt.template.app.navigation.AppNavigator
import com.ngoctientnt.template.core.notification.FcmTokenManager
import com.ngoctientnt.template.core.notification.NotificationRouteHandler
import com.ngoctientnt.template.ui.theme.TemplateTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appBackStack: AppBackStack

    @Inject
    lateinit var appNavigator: AppNavigator

    @Inject
    lateinit var fcmTokenManager: FcmTokenManager

    @Inject
    lateinit var notificationRouteHandler: NotificationRouteHandler

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { /* Permission result is handled by the system notification settings. */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()
        fetchFcmToken()
        handleNotificationIntent(intent)

        setContent {
            TemplateTheme {
                App(
                    appBackStack = appBackStack,
                    appNavigator = appNavigator,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun fetchFcmToken() {
        fcmTokenManager.fetchToken()
    }

    private fun handleNotificationIntent(intent: Intent?) {
        notificationRouteHandler.handleIntent(intent, appNavigator)
    }
}
