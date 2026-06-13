package com.ngoctientnt.template.core.notification

import android.content.Context
import com.google.firebase.messaging.RemoteMessage
import com.ngoctientnt.template.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmMessageHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationHelper: NotificationHelper,
) {
    fun handleMessage(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]

        if (title.isNullOrBlank() && body.isNullOrBlank()) return

        notificationHelper.showNotification(
            title = title ?: context.getString(R.string.app_name),
            body = body.orEmpty(),
            data = message.data,
            imageUrl = resolveImageUrl(message),
        )
    }

    private fun resolveImageUrl(message: RemoteMessage): String? {
        return message.notification?.imageUrl?.toString()?.takeIf { it.isNotBlank() }
            ?: message.data["image"]?.takeIf { it.isNotBlank() }
            ?: message.data["imageUrl"]?.takeIf { it.isNotBlank() }
    }
}
