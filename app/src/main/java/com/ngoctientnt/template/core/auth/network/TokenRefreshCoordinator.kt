package com.ngoctientnt.template.core.auth.network

import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.auth.domain.usecase.RefreshTokenUseCase
import com.ngoctientnt.template.core.auth.session.SessionManager
import com.ngoctientnt.template.core.logging.AppLogger
import com.ngoctientnt.template.core.network.result.ApiResult
import com.ngoctientnt.template.core.network.result.isError
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class TokenRefreshCoordinator @Inject constructor(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val sessionManager: SessionManager,
) {
    private val refreshMutex = Mutex()
    private var ongoingRefresh: CompletableDeferred<ApiResult<AuthTokens>>? = null

    suspend fun refreshSingleFlight(currentRefreshToken: String): ApiResult<AuthTokens> {
        refreshMutex.withLock {
            ongoingRefresh?.let { return it.await() }
        }

        val deferred = CompletableDeferred<ApiResult<AuthTokens>>()
        refreshMutex.withLock {
            ongoingRefresh?.let { return it.await() }
            ongoingRefresh = deferred
        }

        val result = try {
            refreshTokenUseCase(currentRefreshToken).also { refreshResult ->
                if (refreshResult.isError()) {
                    AppLogger.w(TAG, "Token refresh failed — forcing logout")
                    sessionManager.forceLogout()
                }
            }
        } catch (error: CancellationException) {
            refreshMutex.withLock {
                deferred.completeExceptionally(error)
                ongoingRefresh = null
            }
            throw error
        } catch (error: Exception) {
            AppLogger.e(TAG, "Token refresh threw unexpectedly", error)
            sessionManager.forceLogout()
            ApiResult.Unknown(error.message ?: "Token refresh failed")
        }

        refreshMutex.withLock {
            deferred.complete(result)
            ongoingRefresh = null
        }

        return result
    }

    suspend fun awaitOngoingRefresh(): ApiResult<AuthTokens>? {
        val refresh = refreshMutex.withLock { ongoingRefresh } ?: return null
        return refresh.await()
    }

    companion object {
        private const val TAG = "TokenRefreshCoordinator"
    }
}
