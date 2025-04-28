package org.example.travelingapp.data.mapper


import org.example.travelingapp.core.response.user.UserListResponse
import org.example.travelingapp.data.local.entities.UserEntity
import org.example.travelingapp.domain.entities.User

fun UserEntity.toUser() : User {
   return User(
       username = this.username
   )
}

fun UserListResponse.toEntities(): List<UserEntity> =
    data.map { dto ->
        UserEntity(
            id       = dto.id ?: "",
            username = dto.username ?: "",
            email    = dto.email ?: "",
            updateAt = dto.updatedAt
        )
    }
