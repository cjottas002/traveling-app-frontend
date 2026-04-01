package org.example.travelingapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.travelingapp.core.extensions.toQueryMap
import org.example.travelingapp.core.request.destination.CreateDestinationRequest
import org.example.travelingapp.core.request.destination.DestinationRequest
import org.example.travelingapp.core.response.destination.DestinationListResponse
import org.example.travelingapp.core.response.destination.DestinationResponse
import org.example.travelingapp.data.local.daos.DestinationDao
import org.example.travelingapp.data.mapper.toDomain
import org.example.travelingapp.data.mapper.toEntities
import org.example.travelingapp.data.remote.services.IDestinationService
import org.example.travelingapp.domain.entities.Destination
import org.example.travelingapp.domain.repository.IDestinationRepository
import javax.inject.Inject

class DestinationRepository @Inject constructor(
    private val destinationService: IDestinationService,
    private val destinationDao: DestinationDao
) : IDestinationRepository {

    override suspend fun getRemoteDestinations(token: String, request: DestinationRequest): DestinationListResponse {
        val queryMap = request.toQueryMap().toMutableMap()
        request.category?.let { queryMap["category"] = it }
        return destinationService.getDestinations(token, queryMap).body() ?: DestinationListResponse()
    }

    override suspend fun createDestination(token: String, request: CreateDestinationRequest): DestinationResponse {
        return destinationService.createDestination(token, request).body() ?: DestinationResponse()
    }

    override suspend fun syncAndPersist(token: String, request: DestinationRequest) {
        val response = getRemoteDestinations(token, request)
        if (response.success && response.data.isNotEmpty()) {
            destinationDao.deleteAll()
            destinationDao.insertAll(response.toEntities())
        }
    }

    override fun getLocalDestinations(): Flow<List<Destination>> {
        return destinationDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
