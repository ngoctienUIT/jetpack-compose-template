package com.ngoctientnt.template.ui.component.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme

@Composable
fun AppAsyncImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = LocalAppComponentTheme.current.imageShape,
    contentScale: ContentScale = LocalAppComponentTheme.current.imageContentScale,
    alignment: Alignment = Alignment.Center,
    crossfade: Boolean = LocalAppComponentTheme.current.imageCrossfadeEnabled,
    placeholder: @Composable (BoxScope.() -> Unit)? = null,
    error: @Composable (BoxScope.() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
) {
    val clippedModifier = modifier.clip(shape)

    if (url.isNullOrBlank()) {
        Box(modifier = clippedModifier) {
            if (error != null) {
                error()
            } else {
                AppImageDefaults.EmptyPlaceholder()
            }
        }
        return
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(crossfade)
            .listener(
                onSuccess = { _, _ -> onSuccess?.invoke() },
                onError = { _, _ -> onError?.invoke() },
            )
            .build(),
        contentDescription = contentDescription,
        modifier = clippedModifier,
        contentScale = contentScale,
        alignment = alignment,
        loading = {
            if (placeholder != null) {
                placeholder()
            } else {
                AppImageDefaults.LoadingPlaceholder()
            }
        },
        error = {
            if (error != null) {
                error()
            } else {
                AppImageDefaults.ErrorPlaceholder()
            }
        },
    )
}

@Composable
fun AppAvatarImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = LocalAppComponentTheme.current.avatarSize,
    placeholder: @Composable (BoxScope.() -> Unit)? = null,
    error: @Composable (BoxScope.() -> Unit)? = null,
) {
    AppAsyncImage(
        url = url,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        shape = CircleShape,
        contentScale = ContentScale.Crop,
        placeholder = placeholder,
        error = error,
    )
}
