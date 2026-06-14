package com.ngoctientnt.template.ui.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme

object AppImageDefaults {

    @Composable
    fun LoadingPlaceholder(
        modifier: Modifier = Modifier,
        containerColor: Color = LocalAppComponentTheme.current.imagePlaceholderColor,
        indicatorColor: Color = MaterialTheme.colorScheme.primary,
        indicatorSize: Dp = LocalAppComponentTheme.current.imagePlaceholderIconSize,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(containerColor),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(indicatorSize),
                color = indicatorColor,
                strokeWidth = 2.dp,
            )
        }
    }

    @Composable
    fun ErrorPlaceholder(
        modifier: Modifier = Modifier,
        containerColor: Color = LocalAppComponentTheme.current.imageErrorColor,
        icon: ImageVector = Icons.Outlined.BrokenImage,
        iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        iconSize: Dp = LocalAppComponentTheme.current.imagePlaceholderIconSize,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(containerColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = iconTint,
            )
        }
    }

    @Composable
    fun EmptyPlaceholder(
        modifier: Modifier = Modifier,
        containerColor: Color = LocalAppComponentTheme.current.imagePlaceholderColor,
        icon: ImageVector = Icons.Outlined.Image,
        iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        iconSize: Dp = LocalAppComponentTheme.current.imagePlaceholderIconSize,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(containerColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = iconTint,
            )
        }
    }
}
