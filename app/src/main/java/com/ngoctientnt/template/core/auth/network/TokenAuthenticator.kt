package com.ngoctientnt.template.core.auth.network

import com.ngoctientnt.template.core.auth.data.local.SecureTokenStore
import com.ngoctientnt.template.core.auth.network.AuthInterceptor.Companion.AUTHORIZATION_HEADER
import com.ngoctientnt.template.core.auth.network.AuthInterceptor.Companion.BEARER_PREFIX
import com.ngoctientnt.template.core.logging.AppLogger
import com.ngoctientnt.template.core.network.result.getDataOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

@Singleton
class TokenAuthenticator @Inject constructor(
    private val secureTokenStore: SecureTokenStore,
    private val tokenRefreshCoordinator: TokenRefreshCoordinator,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_RETRY_COUNT) {
            AppLogger.w(TAG, "Max auth retries reached for ${response.request.url}")
            return null
        }

        if (response.request.url.encodedPath.contains(AUTH_PATH_SEGMENT)) {
            return null
        }

        val refreshToken = secureTokenStore.readTokensSync()?.refreshToken
        if (refreshToken.isNullOrBlank()) {
            AppLogger.w(TAG, "No refresh token available")
            return null
        }

        val refreshResult = runBlocking {
            val ongoingResult = tokenRefreshCoordinator.awaitOngoingRefresh()
            if (ongoingResult != null) {
                ongoingResult
            } else {
                tokenRefreshCoordinator.refreshSingleFlight(refreshToken)
            }
        }

        val newAccessToken = refreshResult.getDataOrNull()?.accessToken
        if (newAccessToken.isNullOrBlank()) {
            return null
        }

        return response.request.newBuilder()
            .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$newAccessToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }

    companion object {
        private const val TAG = "TokenAuthenticator"
        private const val MAX_RETRY_COUNT = 2
        private const val AUTH_PATH_SEGMENT = "/auth/"
    }
}
