package org.example.travelingapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.travelingapp.core.extensions.toQueryMap
import org.example.travelingapp.core.request.destination.CreateDestinationRequest
import org.example.travelingapp.core.request.destination.DestinationRequest
import org.example.travelingapp.core.request.destination.UpdateDestinationRequest
import org.example.travelingapp.core.response.destination.DestinationListResponse
import org.example.travelingapp.core.response.destination.DestinationResponse
import org.example.travelingapp.data.local.daos.DestinationDao
import org.example.travelingapp.data.local.entities.DestinationEntity
import org.example.travelingapp.data.mapper.toDomain
import org.example.travelingapp.data.mapper.toEntities
import org.example.travelingapp.data.remote.services.IDestinationService
import org.example.travelingapp.data.sync.SyncManager
import org.example.travelingapp.domain.entities.Destination
import org.example.travelingapp.domain.repository.IDestinationRepository
import java.util.UUID
import javax.inject.Inject

class DestinationRepository @Inject constructor(
    private val destinationService: IDestinationService,
    private val destinationDao: DestinationDao,
    private val syncManager: SyncManager
) : IDestinationRepository {

    override suspend fun getRemoteDestinations(token: String, request: DestinationRequest): DestinationListResponse {
        val queryMap = request.toQueryMap().toMutableMap()
        request.category?.let { queryMap["category"] = it }
        return destinationService.getDestinations(token, queryMap).body() ?: DestinationListResponse()
    }

    override suspend fun createDestination(token: String, request: CreateDestinationRequest): DestinationResponse {
        // 1. Save locally immediately
        val localId = UUID.randomUUID().toString()
        val entity = DestinationEntity(
            id = localId,
            name = request.name,
            description = request.description,
            country = request.country,
            imageUrl = request.imageUrl,
            category = request.category,
            createdBy = "",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        destinationDao.insertAll(listOf(entity))

        // 2. Enqueue for backend sync
        syncManager.enqueue("CREATE", "Destination", localId, request)

        return DestinationResponse()
    }

    override suspend fun updateDestination(token: String, request: UpdateDestinationRequest): DestinationResponse {
        // 1. Update locally immediately
        val existing = destinationDao.getById(request.id)
        if (existing != null) {
            val updated = existing.copy(
                name = request.name,
                description = request.description,
                country = request.country,
                imageUrl = request.imageUrl,
                category = request.category,
                updatedAt = System.currentTimeMillis()
            )
            destinationDao.insertAll(listOf(updated))
        }

        // 2. Enqueue for backend sync
        syncManager.enqueue("UPDATE", "Destination", request.id, request)

        return DestinationResponse()
    }

    override suspend fun deleteDestination(token: String, id: String): DestinationResponse {
        // 1. Delete locally immediately
        val entity = destinationDao.getById(id)
        if (entity != null) {
            destinationDao.deleteById(id)
        }

        // 2. Enqueue for backend sync
        syncManager.enqueue("DELETE", "Destination", id)

        return DestinationResponse()
    }

    override suspend fun getDestinationById(token: String, id: String): DestinationResponse {
        return destinationService.getDestinationById(token, id).body() ?: DestinationResponse()
    }

    override suspend fun getLocalDestinationById(id: String): Destination? {
        return destinationDao.getById(id)?.toDomain()
    }

    override suspend fun syncAndPersist(token: String, request: DestinationRequest) {
        val response = getRemoteDestinations(token, request)
        if (response.success) {
            destinationDao.deleteRemoteOnly()
            destinationDao.insertAll(response.toEntities())
        }
    }

    override fun getLocalDestinations(): Flow<List<Destination>> {
        return destinationDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
