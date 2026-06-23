package com.ngoctientnt.template.core.config

data class SocialAuthConfig(
    val googleWebClientId: String,
    val facebookAppId: String,
    val facebookClientToken: String,
) {
    val isGoogleConfigured: Boolean
        get() = googleWebClientId.isNotBlank()

    val isFacebookConfigured: Boolean
        get() = facebookAppId.isNotBlank() &&
            facebookAppId != "0" &&
            facebookClientToken.isNotBlank() &&
            facebookClientToken != "0"
}
