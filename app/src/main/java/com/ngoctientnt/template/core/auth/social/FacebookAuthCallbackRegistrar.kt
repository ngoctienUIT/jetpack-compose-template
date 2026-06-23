package com.ngoctientnt.template.core.auth.social

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FacebookAuthCallbackRegistrar @Inject constructor() {
    val callbackManager: CallbackManager = CallbackManager.Factory.create()

    private val loginManager = LoginManager.getInstance()
    private var loginLauncher: ActivityResultLauncher<Collection<String>>? = null

    fun register(activity: ComponentActivity) {
        loginLauncher = activity.registerForActivityResult(
            loginManager.createLogInActivityResultContract(callbackManager),
        ) {
            // Login result is delivered through LoginManager.registerCallback.
        }
    }

    fun launchLogin(permissions: Collection<String>) {
        val launcher = loginLauncher
            ?: error("Facebook login launcher is not registered. Call register() from MainActivity.onCreate.")
        launcher.launch(permissions)
    }
}
