package com.ngoctientnt.template.core.auth.network

import com.ngoctientnt.template.core.auth.data.local.SecureTokenStore
import com.ngoctientnt.template.core.auth.network.annotation.NoAuth
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

@Singleton
class AuthInterceptor @Inject constructor(
    private val secureTokenStore: SecureTokenStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.isNoAuth()) {
            return chain.proceed(request)
        }

        val accessToken = secureTokenStore.readTokensSync()?.accessToken
        if (accessToken.isNullOrBlank()) {
            return chain.proceed(request)
        }

        val authenticatedRequest = request.newBuilder()
            .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }

    private fun okhttp3.Request.isNoAuth(): Boolean {
        val invocation = tag(Invocation::class.java) ?: return false
        return invocation.method().getAnnotation(NoAuth::class.java) != null
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
    }
}
