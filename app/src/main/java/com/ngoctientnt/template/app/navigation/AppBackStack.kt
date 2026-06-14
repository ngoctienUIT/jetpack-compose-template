package com.ngoctientnt.template.app.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class AppBackStack {

    private var navBackStack: NavBackStack<NavKey>? = null
    private val pendingOperations = mutableListOf<() -> Unit>()

    val backStack: NavBackStack<NavKey>
        get() = requireNotNull(navBackStack) {
            "NavBackStack is not attached yet. Ensure AppNavHost is composed."
        }

    fun attach(stack: NavBackStack<NavKey>) {
        navBackStack = stack
        pendingOperations.forEach { operation -> operation() }
        pendingOperations.clear()
    }

    fun navigate(route: NavKey) {
        runWhenAttached { it.add(route) }
    }

    fun replace(route: NavKey) {
        runWhenAttached {
            it.removeLastOrNull()
            it.add(route)
        }
    }

    fun replaceAll(route: NavKey) {
        runWhenAttached {
            it.clear()
            it.add(route)
        }
    }

    fun pop() {
        runWhenAttached { stack ->
            if (stack.size > 1) {
                stack.removeAt(stack.lastIndex)
            }
        }
    }

    fun popToRoot() {
        runWhenAttached { stack ->
            if (stack.size <= 1) return@runWhenAttached
            val root = stack.first()
            stack.clear()
            stack.add(root)
        }
    }

    val canPop: Boolean
        get() = (navBackStack?.size ?: 0) > 1

    private inline fun runWhenAttached(crossinline block: (NavBackStack<NavKey>) -> Unit) {
        val stack = navBackStack
        if (stack != null) {
            block(stack)
        } else {
            pendingOperations.add {
                navBackStack?.let(block)
            }
        }
    }
}
