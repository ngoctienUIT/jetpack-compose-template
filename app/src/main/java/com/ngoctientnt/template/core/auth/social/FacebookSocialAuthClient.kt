package com.ngoctientnt.template.core.auth.social

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.core.config.SocialAuthConfig
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

@Singleton
class FacebookSocialAuthClient @Inject constructor(
    private val socialAuthConfig: SocialAuthConfig,
    private val facebookAuthCallbackRegistrar: FacebookAuthCallbackRegistrar,
) {
    suspend fun signIn(
        activity: androidx.activity.ComponentActivity,
    ): Result<SocialIdentity> {
        if (!socialAuthConfig.isFacebookConfigured) {
            return Result.failure(SocialAuthException.NotConfigured)
        }

        return suspendCancellableCoroutine { continuation ->
            val loginManager = LoginManager.getInstance()
            val callback = object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val accessToken = result.accessToken.token
                    if (accessToken.isBlank()) {
                        continuation.resume(
                            Result.failure(SocialAuthException.Failed("Facebook access token is empty")),
                        )
                    } else {
                        continuation.resume(
                            Result.success(
                                SocialIdentity(
                                    provider = SocialProvider.FACEBOOK,
                                    accessToken = accessToken,
                                ),
                            ),
                        )
                    }
                }

                override fun onCancel() {
                    continuation.resume(Result.failure(SocialAuthException.Cancelled))
                }

                override fun onError(error: FacebookException) {
                    continuation.resume(
                        Result.failure(
                            SocialAuthException.Failed(
                                error.message ?: "Facebook sign-in failed",
                            ),
                        ),
                    )
                }
            }

            loginManager.registerCallback(
                facebookAuthCallbackRegistrar.callbackManager,
                callback,
            )

            continuation.invokeOnCancellation {
                loginManager.unregisterCallback(facebookAuthCallbackRegistrar.callbackManager)
            }

            facebookAuthCallbackRegistrar.launchLogin(FACEBOOK_PERMISSIONS)
        }
    }

    companion object {
        private val FACEBOOK_PERMISSIONS = listOf("email", "public_profile")
    }
}
