package org.example.travelingapp.domain.entities.hotelmodel

import com.google.gson.annotations.SerializedName

data class Features(
    @SerializedName("paymentPreference") val paymentPreference: Boolean,
    @SerializedName("noCCRequired") val noCCRequired: Boolean
)
