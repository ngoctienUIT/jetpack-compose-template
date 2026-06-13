package com.ngoctientnt.template.app.navigation

interface AppNavigator {

    fun navigate(route: Any)

    fun replace(route: Any)

    fun replaceAll(route: Any)

    fun pop()

    fun popToRoot()

    val canPop: Boolean
}