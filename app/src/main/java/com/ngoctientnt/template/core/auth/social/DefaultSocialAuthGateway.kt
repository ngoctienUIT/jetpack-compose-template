package com.ngoctientnt.template.core.auth.social

import androidx.activity.ComponentActivity
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.core.config.SocialAuthConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSocialAuthGateway @Inject constructor(
    private val socialAuthConfig: SocialAuthConfig,
    private val googleSocialAuthClient: GoogleSocialAuthClient,
    private val facebookSocialAuthClient: FacebookSocialAuthClient,
) : SocialAuthGateway {

    override suspend fun authenticate(
        provider: SocialProvider,
        activity: ComponentActivity,
    ): Result<SocialIdentity> {
        return when (provider) {
            SocialProvider.GOOGLE -> googleSocialAuthClient.signIn(activity)
            SocialProvider.FACEBOOK -> facebookSocialAuthClient.signIn(activity)
        }
    }

    override fun isProviderConfigured(provider: SocialProvider): Boolean {
        return when (provider) {
            SocialProvider.GOOGLE -> socialAuthConfig.isGoogleConfigured
            SocialProvider.FACEBOOK -> socialAuthConfig.isFacebookConfigured
        }
    }
}
