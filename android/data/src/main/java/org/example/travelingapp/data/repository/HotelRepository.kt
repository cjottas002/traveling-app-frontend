package org.example.travelingapp.data.repository


import org.example.travelingapp.domain.entities.hotelmodel.Hotel
import org.example.travelingapp.domain.repository.IHotelRepository
import org.example.travelingapp.data.remote.services.IHotelService
import javax.inject.Inject

class HotelRepository @Inject constructor(private val hotelService: IHotelService) : IHotelRepository {
    override suspend fun fetchHotels(): Result<List<Hotel>> {
        return try {
            val resp = hotelService.getHotels()
            if (resp.isSuccessful) {
                val body = resp.body()
                if (body != null) {
                    Result.success(body.results)
                } else {
                    Result.failure(Exception("Empty response"))
                }
            } else {
                Result.failure(Exception("Error fetching hotels: ${resp.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error. Please try again.", e))
        }
    }
}