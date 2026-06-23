package com.ngoctientnt.template.core.auth.domain.model

data class SocialIdentity(
    val provider: SocialProvider,
    val idToken: String? = null,
    val accessToken: String? = null,
) {
    init {
        require(idToken != null || accessToken != null) {
            "SocialIdentity requires at least one token"
        }
    }
}
