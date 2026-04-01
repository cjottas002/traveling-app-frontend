package org.example.travelingapp.data.mapper

import org.example.travelingapp.core.response.destination.DestinationListResponse
import org.example.travelingapp.data.local.entities.DestinationEntity
import org.example.travelingapp.domain.entities.Destination

fun DestinationListResponse.toEntities(): List<DestinationEntity> =
    data.map { dto ->
        DestinationEntity(
            id = dto.id ?: "",
            name = dto.name ?: "",
            description = dto.description ?: "",
            country = dto.country ?: "",
            imageUrl = dto.imageUrl ?: "",
            category = dto.category ?: "",
            createdBy = dto.createdBy ?: "",
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

fun DestinationEntity.toDomain(): Destination =
    Destination(
        id = id,
        name = name,
        description = description,
        country = country,
        imageUrl = imageUrl,
        category = category,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
