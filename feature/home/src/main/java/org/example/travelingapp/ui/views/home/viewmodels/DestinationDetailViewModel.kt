package org.example.travelingapp.ui.views.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.travelingapp.core.datastore.TokenManager
import org.example.travelingapp.core.request.destination.UpdateDestinationRequest
import org.example.travelingapp.domain.repository.IDestinationRepository
import javax.inject.Inject

@HiltViewModel
class DestinationDetailViewModel @Inject constructor(
    private val destinationRepository: IDestinationRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _country = MutableStateFlow("")
    val country: StateFlow<String> = _country.asStateFlow()

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl.asStateFlow()

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var destinationId: String = ""
    private var loaded = false

    fun loadIfNeeded(id: String) {
        if (loaded && destinationId == id) return
        loaded = true
        destinationId = id
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. Load from Room immediately
            val local = destinationRepository.getLocalDestinationById(destinationId)
            if (local != null) {
                applyData(local.name, local.description, local.country, local.imageUrl, local.category)
            }

            // 2. Try to refresh from backend
            val token = tokenManager.fetchToken()
            if (token != null && token != "offline-session") {
                runCatching {
                    val response = destinationRepository.getDestinationById("Bearer $token", destinationId)
                    if (response.success && response.data != null) {
                        val d = response.data!!
                        applyData(d.name ?: "", d.description ?: "", d.country ?: "", d.imageUrl ?: "", d.category ?: "")
                    }
                }
            }

            _isLoading.value = false
        }
    }

    private fun applyData(name: String, description: String, country: String, imageUrl: String, category: String) {
        _name.value = name
        _description.value = description
        _country.value = country
        _imageUrl.value = imageUrl
        _category.value = category
    }

    fun onNameChanged(value: String) { _name.value = value }
    fun onDescriptionChanged(value: String) { _description.value = value }
    fun onCountryChanged(value: String) { _country.value = value }
    fun onImageUrlChanged(value: String) { _imageUrl.value = value }
    fun onCategoryChanged(value: String) { _category.value = value }

    fun startEdit() { _isEditing.value = true }
    fun cancelEdit() { _isEditing.value = false; load() }

    fun save(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val token = tokenManager.fetchToken()
            if (token == null || token == "offline-session") {
                onError("You need to be online to edit destinations")
                return@launch
            }
            runCatching {
                val request = UpdateDestinationRequest(
                    id = destinationId,
                    name = _name.value,
                    description = _description.value,
                    country = _country.value,
                    imageUrl = _imageUrl.value,
                    category = _category.value
                )
                val response = destinationRepository.updateDestination("Bearer $token", request)
                if (response.success) onSuccess()
                else onError(response.errors.joinToString("\n") { it.message })
            }.onFailure {
                onError(it.message ?: "Error updating destination")
            }
        }
    }

    fun delete(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val token = tokenManager.fetchToken()
            if (token == null || token == "offline-session") {
                onError("You need to be online to delete destinations")
                return@launch
            }
            runCatching {
                val response = destinationRepository.deleteDestination("Bearer $token", destinationId)
                if (response.success) onSuccess()
                else onError(response.errors.joinToString("\n") { it.message })
            }.onFailure {
                onError(it.message ?: "Error deleting destination")
            }
        }
    }
}
