package com.ngoctientnt.template.feature.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ngoctientnt.template.app.navigation.BottomNavTab
import com.ngoctientnt.template.app.navigation.FavoriteRoute
import com.ngoctientnt.template.app.navigation.LocalAppNavigator
import com.ngoctientnt.template.feature.activity.ActivityScreen
import com.ngoctientnt.template.feature.explore.ExploreScreen
import com.ngoctientnt.template.feature.home.HomeScreen
import com.ngoctientnt.template.feature.profile.ProfileScreen
import com.ngoctientnt.template.ui.component.NotchedBottomNavigationBar
import com.ngoctientnt.template.ui.component.NotchedBottomNavigationBarDefaults

@Composable
fun MainScreen(
    initialTab: BottomNavTab = BottomNavTab.HOME,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navigator = LocalAppNavigator.current
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    LaunchedEffect(initialTab) {
        viewModel.applyInitialTabOnce(initialTab)
    }

    BackHandler(enabled = selectedTab != BottomNavTab.HOME) {
        viewModel.selectTab(BottomNavTab.HOME)
    }

    val navBarInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val contentBottomPadding = NotchedBottomNavigationBarDefaults.TotalHeight + navBarInset

    Box(modifier = Modifier.fillMaxSize()) {
        PersistentTabHost(
            selectedTab = selectedTab,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = contentBottomPadding),
        )

        NotchedBottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = viewModel::selectTab,
            onFavoriteClick = { navigator.navigate(FavoriteRoute) },
            windowInsetsBottom = navBarInset,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun PersistentTabHost(
    selectedTab: BottomNavTab,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        BottomNavTab.entries.forEach { tab ->
            key(tab) {
                val visible = selectedTab == tab
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(if (visible) 1f else 0f)
                        .graphicsLayer { alpha = if (visible) 1f else 0f },
                ) {
                    TabContent(tab = tab)
                }
            }
        }
    }
}

@Composable
private fun TabContent(tab: BottomNavTab) {
    when (tab) {
        BottomNavTab.HOME -> HomeScreen()
        BottomNavTab.EXPLORE -> ExploreScreen()
        BottomNavTab.ACTIVITY -> ActivityScreen()
        BottomNavTab.PROFILE -> ProfileScreen()
    }
}
