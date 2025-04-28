package org.example.travelingapp.domain.entities.hotelmodel

import com.google.gson.annotations.SerializedName

data class RatePlan(
    @SerializedName("price") val price: Price,
    @SerializedName("features") val features: Features
)
