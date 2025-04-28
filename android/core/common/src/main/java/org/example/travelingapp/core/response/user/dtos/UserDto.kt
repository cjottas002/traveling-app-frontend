package org.example.travelingapp.core.response.user.dtos

import org.example.travelingapp.core.response.ResponseDto

data class UserDto(
    val id: String?,
    val username: String?,
    val email: String?,
    val updatedAt: Long
) : ResponseDto()
