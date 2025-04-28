package org.example.travelingapp.domain.entities.hotelmodel

import com.google.gson.annotations.SerializedName

data class Coordinate(@SerializedName("lat") val lat: Double, @SerializedName("lon") val lon: Double)