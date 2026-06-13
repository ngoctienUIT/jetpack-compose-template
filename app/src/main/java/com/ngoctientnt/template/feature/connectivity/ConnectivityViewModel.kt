package com.ngoctientnt.template.feature.connectivity

import androidx.lifecycle.ViewModel
import com.ngoctientnt.template.core.network.NetworkConnectivityObserver
import com.ngoctientnt.template.core.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    networkConnectivityObserver: NetworkConnectivityObserver,
) : ViewModel() {

    val networkStatus: StateFlow<NetworkStatus> = networkConnectivityObserver.networkStatus

    private val observer = networkConnectivityObserver

    fun refreshNetworkStatus() {
        observer.refresh()
    }
}
