package org.example.travelingapp.data.remote.hotelmodel

import com.google.gson.annotations.SerializedName
import org.example.travelingapp.data.remote.Pagination
import org.example.travelingapp.domain.entities.hotelmodel.Hotel

data class HotelResponse(
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("results") val results: List<Hotel>,
    @SerializedName("pagination") val pagination: Pagination?
)