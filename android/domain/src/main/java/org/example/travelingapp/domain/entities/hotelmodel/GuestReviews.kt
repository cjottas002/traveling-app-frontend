package org.example.travelingapp.domain.entities.hotelmodel


import com.google.gson.annotations.SerializedName

data class GuestReviews(
    @SerializedName("unformattedRating") val unformattedRating: Double,
    @SerializedName("rating") val rating: String,
    @SerializedName("total") val total: Int,
    @SerializedName("scale") val scale: Int,
    @SerializedName("badge") val badge: String?,
    @SerializedName("badgeText") val badgeText: String?
)
