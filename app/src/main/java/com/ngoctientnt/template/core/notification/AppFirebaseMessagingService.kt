package com.ngoctientnt.template.core.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var fcmMessageHandler: FcmMessageHandler

    @Inject
    lateinit var fcmTokenManager: FcmTokenManager

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        fcmMessageHandler.handleMessage(message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        fcmTokenManager.onNewToken(token)
    }
}
