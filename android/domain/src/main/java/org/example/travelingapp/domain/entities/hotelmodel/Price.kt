package org.example.travelingapp.domain.entities.hotelmodel


import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("current") val current: String,
    @SerializedName("exactCurrent") val exactCurrent: Double,
    @SerializedName("old") val old: String?
)
