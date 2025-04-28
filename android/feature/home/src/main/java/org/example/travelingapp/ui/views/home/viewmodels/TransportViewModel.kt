package org.example.travelingapp.ui.views.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.travelingapp.domain.entities.Transport
import org.example.travelingapp.domain.repository.ITransportRepository
import javax.inject.Inject

@HiltViewModel
class TransportViewModel @Inject constructor(private val transportRepository: ITransportRepository) : ViewModel() {
    private val _transports = MutableStateFlow<List<Transport>>(emptyList())
    val transports: StateFlow<List<Transport>> get() = _transports

    init {
        viewModelScope.launch {
            transportRepository.getTransports()
                .collect { listOfTransport ->
                    _transports.value = listOfTransport
                }
        }
    }
}