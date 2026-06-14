package com.ngoctientnt.template.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ngoctientnt.template.feature.detail.DetailScreen
import com.ngoctientnt.template.feature.favorite.FavoriteScreen
import com.ngoctientnt.template.feature.login.LoginScreen
import com.ngoctientnt.template.feature.main.MainScreen
import com.ngoctientnt.template.feature.splash.SplashScreen

@Composable
fun AppNavHost(
    appBackStack: AppBackStack,
    appNavigator: AppNavigator,
) {
    CompositionLocalProvider(LocalAppNavigator provides appNavigator) {
        NavDisplay(
            backStack = appBackStack.backStack,
            onBack = appBackStack::pop,
            transitionSpec = AppNavTransitions.push,
            popTransitionSpec = AppNavTransitions.pop,
            predictivePopTransitionSpec = AppNavTransitions.predictivePop,
            entryProvider = entryProvider {
                entry<SplashRoute> {
                    SplashScreen()
                }

                entry<LoginRoute> {
                    LoginScreen()
                }

                entry<MainRoute> { route ->
                    MainScreen(initialTab = BottomNavTab.fromName(route.tab))
                }

                entry<DetailRoute> { route ->
                    DetailScreen(id = route.id)
                }

                entry<FavoriteRoute> {
                    FavoriteScreen()
                }
            },
        )
    }
}
