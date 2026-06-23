package com.ngoctientnt.template.ui.component.auth

import androidx.activity.ComponentActivity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.core.auth.social.SocialAuthException
import com.ngoctientnt.template.core.auth.social.SocialAuthGateway
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialAuthLauncher @Inject constructor(
    private val socialAuthGateway: SocialAuthGateway,
) {
    suspend fun authenticate(
        provider: SocialProvider,
        activity: ComponentActivity,
    ): SocialAuthLaunchResult {
        if (!socialAuthGateway.isProviderConfigured(provider)) {
            return SocialAuthLaunchResult.Error(SocialAuthErrorCodes.NOT_CONFIGURED)
        }

        return socialAuthGateway.authenticate(provider, activity)
            .fold(
                onSuccess = { SocialAuthLaunchResult.Success(it) },
                onFailure = { error ->
                    when (error) {
                        is SocialAuthException.Cancelled -> SocialAuthLaunchResult.Cancelled
                        is SocialAuthException.NotConfigured ->
                            SocialAuthLaunchResult.Error(SocialAuthErrorCodes.NOT_CONFIGURED)
                        is SocialAuthException.Failed ->
                            SocialAuthLaunchResult.Error(SocialAuthErrorCodes.FAILED)
                        else -> SocialAuthLaunchResult.Error(SocialAuthErrorCodes.FAILED)
                    }
                },
            )
    }
}
