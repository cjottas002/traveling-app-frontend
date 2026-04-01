package org.example.travelingapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.travelingapp.core.request.destination.CreateDestinationRequest
import org.example.travelingapp.core.request.destination.DestinationRequest
import org.example.travelingapp.core.response.destination.DestinationListResponse
import org.example.travelingapp.core.response.destination.DestinationResponse
import org.example.travelingapp.domain.entities.Destination

interface IDestinationRepository {
    suspend fun getRemoteDestinations(token: String, request: DestinationRequest): DestinationListResponse
    suspend fun createDestination(token: String, request: CreateDestinationRequest): DestinationResponse
    suspend fun syncAndPersist(token: String, request: DestinationRequest)
    fun getLocalDestinations(): Flow<List<Destination>>
}
