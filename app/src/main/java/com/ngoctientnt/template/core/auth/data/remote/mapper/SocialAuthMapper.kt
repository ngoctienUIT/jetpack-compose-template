package com.ngoctientnt.template.core.auth.data.remote.mapper

import com.ngoctientnt.template.core.auth.data.remote.dto.SocialAuthRequestDto
import com.ngoctientnt.template.core.auth.domain.model.SocialAuthIntent
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider

fun SocialIdentity.toRequestDto(intent: SocialAuthIntent): SocialAuthRequestDto {
    return SocialAuthRequestDto(
        provider = provider.toApiValue(),
        idToken = idToken,
        accessToken = accessToken,
        intent = intent.toApiValue(),
    )
}

private fun SocialProvider.toApiValue(): String {
    return when (this) {
        SocialProvider.GOOGLE -> "google"
        SocialProvider.FACEBOOK -> "facebook"
    }
}

private fun SocialAuthIntent.toApiValue(): String {
    return when (this) {
        SocialAuthIntent.LOGIN -> "login"
        SocialAuthIntent.SIGN_UP -> "signup"
    }
}
