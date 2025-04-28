package org.example.travelingapp.domain.repository

import org.example.travelingapp.domain.entities.hotelmodel.Hotel


interface IHotelRepository {
    suspend fun fetchHotels(): Result<List<Hotel>>;
}