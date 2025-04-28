package org.example.travelingapp.data.remote

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("pageGroup") val pageGroup: String,
    @SerializedName("nextPageStartIndex") val nextPageStartIndex: Int,
    @SerializedName("nextPageNumber") val nextPageNumber: Int,
    @SerializedName("nextPageGroup") val nextPageGroup: String
)
