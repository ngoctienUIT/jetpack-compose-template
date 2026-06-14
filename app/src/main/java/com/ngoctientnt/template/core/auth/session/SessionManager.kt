package com.ngoctientnt.template.core.auth.session

import com.ngoctientnt.template.app.navigation.AppNavigator
import com.ngoctientnt.template.app.navigation.LoginRoute
import com.ngoctientnt.template.core.auth.domain.model.SessionState
import com.ngoctientnt.template.core.auth.domain.repository.AuthRepository
import com.ngoctientnt.template.core.logging.AppLogger
import com.ngoctientnt.template.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class SessionManager @Inject constructor(
    private val authRepository: AuthRepository,
    private val appNavigator: AppNavigator,
    @ApplicationScope private val applicationScope: CoroutineScope,
) {
    private val logoutMutex = Mutex()

    private val sessionState: Flow<SessionState> = authRepository.observeTokens()
        .map { tokens ->
            if (tokens != null) {
                SessionState.Authenticated
            } else {
                SessionState.Unauthenticated
            }
        }
        .distinctUntilChanged()

    fun observeSession(): Flow<SessionState> = sessionState

    suspend fun logout() {
        logoutMutex.withLock {
            authRepository.clearTokens()
        }
        appNavigator.replaceAll(LoginRoute)
    }

    fun forceLogout() {
        applicationScope.launch {
            logoutMutex.withLock {
                runCatching { authRepository.clearTokens() }
                    .onFailure { error ->
                        AppLogger.e(TAG, "Failed to clear tokens during force logout", error)
                    }
            }
            appNavigator.replaceAll(LoginRoute)
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return authRepository.getAccessToken() != null
    }

    companion object {
        private const val TAG = "SessionManager"
    }
}
