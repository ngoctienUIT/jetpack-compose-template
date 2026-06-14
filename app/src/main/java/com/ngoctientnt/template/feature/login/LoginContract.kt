package com.ngoctientnt.template.feature.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface LoginIntent {
    data class EmailChanged(val value: String) : LoginIntent

    data class PasswordChanged(val value: String) : LoginIntent

    data object SignInClicked : LoginIntent
}

sealed interface LoginEffect {
    data object NavigateToMain : LoginEffect
}

object LoginErrors {
    const val EMPTY_FIELDS = "empty_fields"
}
