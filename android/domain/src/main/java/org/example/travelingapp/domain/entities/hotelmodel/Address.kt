package org.example.travelingapp.domain.entities.hotelmodel

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("streetAddress") val streetAddress: String,
    @SerializedName("extendedAddress") val extendedAddress: String?,
    @SerializedName("locality") val locality: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("region") val region: String,
    @SerializedName("countryName") val countryName: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("obfuscate") val obfuscate: Boolean
)
