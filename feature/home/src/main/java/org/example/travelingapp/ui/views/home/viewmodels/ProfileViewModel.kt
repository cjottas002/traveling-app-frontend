package org.example.travelingapp.ui.views.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.travelingapp.core.datastore.TokenManager
import org.example.travelingapp.core.network.interfaces.INetworkChecker
import org.example.travelingapp.data.sync.SyncManager

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val syncManager: SyncManager,
    private val networkChecker: INetworkChecker
) : ViewModel() {

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username.asStateFlow()

    private val _role = MutableStateFlow<String?>(null)
    val role: StateFlow<String?> = _role.asStateFlow()

    private val _pendingOperations = MutableStateFlow(0)
    val pendingOperations: StateFlow<Int> = _pendingOperations.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        observeSession()
        observePendingOperations()
        refreshConnectivity()
    }

    private fun observeSession() {
        viewModelScope.launch {
            tokenManager.usernameFlow.collect { _username.value = it }
        }
        viewModelScope.launch {
            tokenManager.roleFlow.collect { _role.value = it }
        }
    }

    private fun observePendingOperations() {
        viewModelScope.launch {
            syncManager.observePendingCount().collect { _pendingOperations.value = it }
        }
    }

    private fun refreshConnectivity() {
        _isOnline.value = runCatching { networkChecker.isInternetAvailable() }
            .getOrDefault(false)
    }
}
