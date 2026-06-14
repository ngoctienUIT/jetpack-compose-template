package com.ngoctientnt.template.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngoctientnt.template.core.auth.domain.usecase.LoginUseCase
import com.ngoctientnt.template.core.network.result.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = Channel<LoginEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> reduce { copy(email = intent.value, errorMessage = null) }
            is LoginIntent.PasswordChanged -> reduce { copy(password = intent.value, errorMessage = null) }
            LoginIntent.SignInClicked -> signIn()
        }
    }

    private fun signIn() {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        val email = currentState.email.trim()
        val password = currentState.password

        if (email.isBlank() || password.isBlank()) {
            reduce { copy(errorMessage = LoginErrors.EMPTY_FIELDS) }
            return
        }

        viewModelScope.launch {
            reduce { copy(isLoading = true, errorMessage = null) }
            loginUseCase(email, password).handleResult(
                onSuccess = {
                    reduce { copy(isLoading = false) }
                    _effect.send(LoginEffect.NavigateToMain)
                },
                onError = { errorMessage ->
                    reduce { copy(isLoading = false, errorMessage = errorMessage) }
                },
            )
        }
    }

    private inline fun reduce(block: LoginUiState.() -> LoginUiState) {
        _uiState.update(block)
    }

}
