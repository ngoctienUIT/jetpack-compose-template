package com.ngoctientnt.template.core.auth.social

import androidx.activity.ComponentActivity
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider

interface SocialAuthGateway {
    suspend fun authenticate(
        provider: SocialProvider,
        activity: ComponentActivity,
    ): Result<SocialIdentity>

    fun isProviderConfigured(provider: SocialProvider): Boolean
}
