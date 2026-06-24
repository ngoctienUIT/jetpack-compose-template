package com.ngoctientnt.template.core.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base class for ViewModels that follow the MVI (Model-View-Intent) pattern.
 *
 * @param S The type of the UI state.
 * @param I The type of the user intent.
 * @param E The type of the side effect.
 */
abstract class BaseViewModel<S, I, E>(initialState: S) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    protected val currentState: S get() = uiState.value

    /**
     * Handles user intents.
     */
    abstract fun onIntent(intent: I)

    /**
     * Updates the UI state.
     */
    protected fun reduce(block: S.() -> S) {
        _uiState.update(block)
    }

    /**
     * Sends a side effect.
     */
    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
