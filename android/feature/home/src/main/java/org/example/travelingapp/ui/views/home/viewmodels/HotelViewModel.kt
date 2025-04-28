package org.example.travelingapp.ui.views.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.travelingapp.domain.entities.hotelmodel.Hotel
import org.example.travelingapp.domain.repository.IHotelRepository
import javax.inject.Inject

@HiltViewModel
class HotelViewModel @Inject constructor(private val repo: IHotelRepository) : ViewModel() {

    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())
    val hotels: StateFlow<List<Hotel>> = _hotels.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadHotels()
    }

    fun loadHotels() {
        viewModelScope.launch {
            repo.fetchHotels().fold(
                onSuccess = { list ->
                    _hotels.value = list
                    _error.value = null
                },
                onFailure = { ex ->
                    _hotels.value = emptyList()
                    _error.value = ex.message
                }
            )
        }
    }
}