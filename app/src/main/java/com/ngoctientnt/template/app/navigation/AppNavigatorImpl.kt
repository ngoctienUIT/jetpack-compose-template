package com.ngoctientnt.template.app.navigation

import androidx.navigation3.runtime.NavKey

class AppNavigatorImpl(
    private val appBackStack: AppBackStack,
) : AppNavigator {

    override fun navigate(route: Any) {
        appBackStack.navigate(route as NavKey)
    }

    override fun replace(route: Any) {
        appBackStack.replace(route as NavKey)
    }

    override fun replaceAll(route: Any) {
        appBackStack.replaceAll(route as NavKey)
    }

    override fun pop() {
        appBackStack.pop()
    }

    override fun popToRoot() {
        appBackStack.popToRoot()
    }

    override val canPop: Boolean
        get() = appBackStack.canPop
}
