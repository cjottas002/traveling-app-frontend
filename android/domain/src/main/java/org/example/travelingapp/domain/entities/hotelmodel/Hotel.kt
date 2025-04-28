package org.example.travelingapp.domain.entities.hotelmodel


import com.google.gson.annotations.SerializedName

data class Hotel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("starRating") val starRating: Int,
    @SerializedName("address") val address: Address,
    @SerializedName("guestReviews") val guestReviews: GuestReviews,
    @SerializedName("ratePlan") val ratePlan: RatePlan,
    @SerializedName("neighbourhood") val neighbourhood: String,
    @SerializedName("coordinate") val coordinate: Coordinate,
    @SerializedName("providerType") val providerType: String,
    @SerializedName("supplierHotelId") val supplierHotelId: Int,
    @SerializedName("isAlternative") val isAlternative: Boolean,
    @SerializedName("optimizedThumbUrls") val optimizedThumbUrls: OptimizedThumbUrls
) {
    override fun toString(): String {
        return "Hotel(id=$id, name='$name', starRating=$starRating, address=$address, " +
                "guestReviews=$guestReviews, ratePlan=$ratePlan, neighbourhood='$neighbourhood', " +
                "coordinate=$coordinate, providerType='$providerType', supplierHotelId=$supplierHotelId, " +
                "isAlternative=$isAlternative, optimizedThumbUrls=$optimizedThumbUrls)"
    }
}
