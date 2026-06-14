package com.ngoctientnt.template.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngoctientnt.template.core.auth.domain.model.SessionState
import com.ngoctientnt.template.core.auth.domain.usecase.ObserveSessionUseCase
import com.ngoctientnt.template.core.config.AppConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
) : ViewModel() {

    private val sessionState = observeSessionUseCase()

    private val _effect = Channel<SplashEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(AppConfig.SPLASH_DELAY_MS)
            val destination = when (sessionState.first()) {
                SessionState.Authenticated -> SplashEffect.NavigateToMain
                SessionState.Loading,
                SessionState.Unauthenticated,
                -> SplashEffect.NavigateToLogin
            }
            _effect.send(destination)
        }
    }
}

sealed interface SplashEffect {
    data object NavigateToMain : SplashEffect

    data object NavigateToLogin : SplashEffect
}
