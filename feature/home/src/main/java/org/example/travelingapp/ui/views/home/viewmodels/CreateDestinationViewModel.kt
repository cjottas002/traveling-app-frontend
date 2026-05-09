package org.example.travelingapp.ui.views.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.travelingapp.core.datastore.TokenManager
import org.example.travelingapp.core.request.destination.CreateDestinationRequest
import org.example.travelingapp.domain.repository.IDestinationRepository
import javax.inject.Inject

@HiltViewModel
class CreateDestinationViewModel @Inject constructor(
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

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    val isEnabled: StateFlow<Boolean> = combine(_name, _country, _category, _isSaving) { name, country, category, isSaving ->
        name.isNotBlank() && country.isNotBlank() && category.isNotBlank() && !isSaving
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun onNameChanged(value: String) { _name.value = value }
    fun onDescriptionChanged(value: String) { _description.value = value }
    fun onCountryChanged(value: String) { _country.value = value }
    fun onImageUrlChanged(value: String) { _imageUrl.value = value }
    fun onCategoryChanged(value: String) { _category.value = value }

    fun create(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_isSaving.value) return

        viewModelScope.launch {
            val token = tokenManager.fetchToken()
            if (token == null || token == "offline-session") {
                onError("You need to be online to create destinations")
                return@launch
            }

            _isSaving.value = true
            runCatching {
                val request = CreateDestinationRequest(
                    name = _name.value,
                    description = _description.value,
                    country = _country.value,
                    imageUrl = _imageUrl.value,
                    category = _category.value
                )
                val response = destinationRepository.createDestination("Bearer $token", request)
                if (response.success) {
                    onSuccess()
                } else {
                    onError(response.errors.joinToString("\n") { it.message })
                }
            }.onFailure {
                onError(it.message ?: "Error creating destination")
            }.also {
                _isSaving.value = false
            }
        }
    }
}
