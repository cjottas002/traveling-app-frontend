package org.example.travelingapp.data.remote.services

import org.example.travelingapp.data.remote.hotelmodel.HotelResponse
import retrofit2.Response
import retrofit2.http.GET

interface IHotelService {
    @GET("listHotels")
    suspend fun getHotels(): Response<HotelResponse>
}