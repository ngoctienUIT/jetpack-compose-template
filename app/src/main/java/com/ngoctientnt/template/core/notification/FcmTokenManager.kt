package com.ngoctientnt.template.core.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor() {

    fun fetchToken(onResult: (String?) -> Unit = {}) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Failed to fetch FCM token", task.exception)
                    onResult(null)
                    return@addOnCompleteListener
                }
                onResult(task.result)
            }
    }

    fun onNewToken(token: String) {
        Log.d(TAG, "FCM token refreshed: $token")
        // TODO: Send token to your backend when API is ready.
    }

    companion object {
        private const val TAG = "FcmTokenManager"
    }
}
