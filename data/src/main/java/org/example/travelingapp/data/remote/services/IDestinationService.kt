package org.example.travelingapp.data.remote.services

import org.example.travelingapp.core.request.destination.CreateDestinationRequest
import org.example.travelingapp.core.response.destination.DestinationListResponse
import org.example.travelingapp.core.response.destination.DestinationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface IDestinationService {

    @GET("api/Destination/List")
    suspend fun getDestinations(
        @Header("Authorization") token: String,
        @QueryMap query: Map<String, String>
    ): Response<DestinationListResponse>

    @POST("api/Destination/Create")
    suspend fun createDestination(
        @Header("Authorization") token: String,
        @Body request: CreateDestinationRequest
    ): Response<DestinationResponse>
}
