package com.ngoctientnt.template.core.notification

import android.content.Intent
import com.ngoctientnt.template.app.navigation.AppNavigator
import com.ngoctientnt.template.app.navigation.DetailRoute
import com.ngoctientnt.template.app.navigation.HomeRoute
import com.ngoctientnt.template.app.navigation.ProfileRoute
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRouteHandler @Inject constructor() {

    fun handleIntent(intent: Intent?, appNavigator: AppNavigator) {
        if (intent?.getBooleanExtra(NotificationHelper.EXTRA_FROM_NOTIFICATION, false) != true) {
            return
        }

        when (intent.getStringExtra(NotificationHelper.EXTRA_ROUTE)) {
            "home" -> appNavigator.navigate(HomeRoute)
            "profile" -> appNavigator.navigate(ProfileRoute)
            "detail" -> {
                val id = intent.getStringExtra(NotificationHelper.EXTRA_DETAIL_ID) ?: return
                appNavigator.navigate(DetailRoute(id = id))
            }
        }

        intent.removeExtra(NotificationHelper.EXTRA_FROM_NOTIFICATION)
    }
}
