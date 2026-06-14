package com.ngoctientnt.template.feature.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ngoctientnt.template.R
import com.ngoctientnt.template.feature.explore.domain.ExploreItem
import com.ngoctientnt.template.ui.component.button.AppOutlinedButton
import com.ngoctientnt.template.ui.component.image.AppAsyncImage
import com.ngoctientnt.template.ui.component.network.resolveApiErrorMessage

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val exploreItems = viewModel.items.collectAsLazyPagingItems()

    // retryKey change triggers Paging retry/refresh
    LaunchedEffect(uiState.retryKey) {
        snapshotFlow { exploreItems.loadState.refresh }
            .collect { loadState ->
                if (loadState is LoadState.Error) {
                    exploreItems.retry()
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(
            text = stringResource(R.string.explore_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.explore_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        )

        ExploreContent(
            exploreItems = exploreItems,
            onRetry = { viewModel.onIntent(ExploreIntent.RetryRequested) },
            onRefresh = { viewModel.onIntent(ExploreIntent.RefreshRequested) },
        )
    }
}

@Composable
private fun ExploreContent(
    exploreItems: LazyPagingItems<ExploreItem>,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
) {
    when (val refreshState = exploreItems.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is LoadState.Error -> {
            ExplorePagingMessage(
                message = refreshState.error.message ?: stringResource(R.string.explore_error),
                onRetry = onRetry,
            )
        }
        is LoadState.NotLoading -> {
            if (exploreItems.itemCount == 0) {
                ExplorePagingMessage(
                    message = stringResource(R.string.explore_empty),
                    onRetry = onRefresh,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        count = exploreItems.itemCount,
                        key = exploreItems.itemKey { it.id },
                    ) { index ->
                        val item = exploreItems[index] ?: return@items
                        ExploreItemRow(item = item)
                    }

                    if (exploreItems.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    if (exploreItems.loadState.append is LoadState.Error) {
                        item {
                            val appendError = exploreItems.loadState.append as LoadState.Error
                            ExplorePagingMessage(
                                message = appendError.error.message
                                    ?: stringResource(R.string.explore_error),
                                onRetry = onRetry,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExploreItemRow(
    item: ExploreItem,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = item.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp),
        )
        item.imageUrl?.let { imageUrl ->
            AppAsyncImage(
                url = imageUrl,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun ExplorePagingMessage(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = resolveApiErrorMessage(message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
        AppOutlinedButton(
            text = stringResource(R.string.explore_retry),
            onClick = onRetry,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}
