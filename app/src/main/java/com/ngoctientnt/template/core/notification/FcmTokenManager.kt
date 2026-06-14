package com.ngoctientnt.template.core.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.ngoctientnt.template.core.logging.AppLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor() {

    fun fetchToken(onResult: (String?) -> Unit = {}) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    AppLogger.w(TAG, "Failed to fetch FCM token", task.exception)
                    onResult(null)
                    return@addOnCompleteListener
                }
                onResult(task.result)
            }
    }

    fun onNewToken(token: String) {
        AppLogger.d(TAG, "FCM token refreshed")
        // TODO: Send token to your backend when API is ready.
    }

    companion object {
        private const val TAG = "FcmTokenManager"
    }
}
