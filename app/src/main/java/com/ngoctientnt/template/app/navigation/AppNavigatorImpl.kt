package com.ngoctientnt.template.app.navigation

class AppNavigatorImpl(
    private val appBackStack: AppBackStack
) : AppNavigator {

    override fun navigate(route: Any) {
        appBackStack.navigate(route)
    }

    override fun replace(route: Any) {
        appBackStack.replace(route)
    }

    override fun replaceAll(route: Any) {
        appBackStack.replaceAll(route)
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