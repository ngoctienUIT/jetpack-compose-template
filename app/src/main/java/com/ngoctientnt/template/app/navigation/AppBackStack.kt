package com.ngoctientnt.template.app.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

@Stable
class AppBackStack {

    private val _backStack =
        mutableStateListOf<Any>(SplashRoute)

    val backStack: SnapshotStateList<Any>
        get() = _backStack

    fun navigate(route: Any) {
        _backStack.add(route)
    }

    fun replace(route: Any) {
        _backStack.removeLastOrNull()
        _backStack.add(route)
    }

    fun replaceAll(route: Any) {
        _backStack.clear()
        _backStack.add(route)
    }

    fun pop() {
        if (_backStack.size > 1) {
            _backStack.removeLastOrNull()
        }
    }

    fun popToRoot() {
        if (_backStack.size <= 1) return
        val root = _backStack.first()
        _backStack.clear()
        _backStack.add(root)
    }

    val canPop: Boolean
        get() = _backStack.size > 1
}