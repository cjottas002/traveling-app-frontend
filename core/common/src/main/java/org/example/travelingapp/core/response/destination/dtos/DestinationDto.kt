package org.example.travelingapp.core.response.destination.dtos

import org.example.travelingapp.core.response.ResponseDto

data class DestinationDto(
    val id: String?,
    val name: String?,
    val description: String?,
    val country: String?,
    val imageUrl: String?,
    val category: String?,
    val createdBy: String?,
    val createdAt: Long,
    val updatedAt: Long
) : ResponseDto()
