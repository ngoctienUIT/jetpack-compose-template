package com.ngoctientnt.template.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ngoctientnt.template.R
import com.ngoctientnt.template.app.navigation.BottomNavTab

@Composable
fun NotchedBottomNavigationBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    onFavoriteClick: () -> Unit,
    windowInsetsBottom: Dp = 0.dp,
    modifier: Modifier = Modifier,
) {
    val defaults = NotchedBottomNavigationBarDefaults
    val barContentHeight = defaults.BarContentHeight
    val fabSize = defaults.FabSize
    val fabOverlap = defaults.FabOverlap
    val notchRadius = defaults.NotchRadius
    val notchGap = defaults.NotchGap
    val notchSpacerWidth = defaults.NotchSpacerWidth
    val favoriteLabel = stringResource(R.string.nav_favorite)
    val boxHeight = fabOverlap + barContentHeight + windowInsetsBottom

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(boxHeight),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(barContentHeight + windowInsetsBottom),
            shape = NotchedBottomBarShape(
                notchRadius = notchRadius,
                notchGap = notchGap,
            ),
            color = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = 3.dp,
            shadowElevation = 8.dp,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(barContentHeight)
                        .selectableGroup(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        BottomNavTab.leftTabs.forEach { tab ->
                            NavBarTabItem(
                                tab = tab,
                                selected = selectedTab == tab,
                                onClick = { onTabSelected(tab) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(notchSpacerWidth))

                    Row(modifier = Modifier.weight(1f)) {
                        BottomNavTab.rightTabs.forEach { tab ->
                            NavBarTabItem(
                                tab = tab,
                                selected = selectedTab == tab,
                                onClick = { onTabSelected(tab) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                if (windowInsetsBottom > 0.dp) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(windowInsetsBottom),
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(fabSize)
                .semantics { contentDescription = favoriteLabel },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = defaults.FabElevation,
                pressedElevation = defaults.FabElevation + 2.dp,
            ),
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun RowScope.NavBarTabItem(
    tab: BottomNavTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        icon = {
            Icon(
                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                contentDescription = null,
            )
        },
        label = {
            Text(
                text = stringResource(tab.labelRes),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    )
}
