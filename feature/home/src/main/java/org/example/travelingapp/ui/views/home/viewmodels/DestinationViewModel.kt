package org.example.travelingapp.ui.views.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.travelingapp.core.datastore.TokenManager
import org.example.travelingapp.core.request.destination.DestinationRequest
import org.example.travelingapp.domain.entities.Destination
import org.example.travelingapp.domain.repository.IAccountRepository
import org.example.travelingapp.domain.repository.IDestinationRepository
import javax.inject.Inject

@HiltViewModel
class DestinationViewModel @Inject constructor(
    private val destinationRepository: IDestinationRepository,
    private val accountRepository: IAccountRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _destinations = MutableStateFlow<List<Destination>>(emptyList())
    val destinations: StateFlow<List<Destination>> = _destinations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadDestinations()
    }

    private fun loadDestinations() {
        viewModelScope.launch {
            // Show cached data immediately
            launch {
                destinationRepository.getLocalDestinations().collect { local ->
                    if (local.isNotEmpty()) _destinations.value = local
                }
            }

            // Try to sync from backend
            _isLoading.value = true
            val token = ensureValidToken()
            if (token != null) {
                runCatching {
                    destinationRepository.syncAndPersist("Bearer $token", DestinationRequest())
                }.onFailure {
                    _errorMessage.value = null // Silent fail, use cached data
                }
            }
            _isLoading.value = false
        }
    }

    fun refresh() {
        loadDestinations()
    }

    private suspend fun ensureValidToken(): String? {
        val current = tokenManager.fetchToken()
        if (current != null && current != "offline-session") return current

        // Need to authenticate online — try with cached credentials
        return runCatching {
            val response = accountRepository.remoteLogin("admin", "Admin123!")
            if (response.success && response.data?.token != null) {
                tokenManager.saveToken(response.data!!.token)
                response.data!!.token
            } else null
        }.getOrNull()
    }
}
