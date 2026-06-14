package com.ngoctientnt.template.core.auth.network

import com.ngoctientnt.template.core.auth.session.SessionManager
import com.ngoctientnt.template.core.logging.AppLogger
import com.ngoctientnt.template.di.AuthenticatedOkHttpClient
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

@Singleton
class ForbiddenInterceptor @Inject constructor(
    @AuthenticatedOkHttpClient private val authenticatedClient: Lazy<OkHttpClient>,
    private val sessionManager: SessionManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code == HTTP_FORBIDDEN && !chain.request().url.encodedPath.contains(AUTH_PATH_SEGMENT)) {
            AppLogger.w(TAG, "403 Forbidden — cancelling in-flight calls and logging out")
            authenticatedClient.get().dispatcher.cancelAll()
            sessionManager.forceLogout()
        }

        return response
    }

    companion object {
        private const val TAG = "ForbiddenInterceptor"
        private const val HTTP_FORBIDDEN = 403
        private const val AUTH_PATH_SEGMENT = "/auth/"
    }
}
