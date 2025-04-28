package org.example.travelingapp.domain.repository

import org.example.travelingapp.core.request.user.UserRequest
import org.example.travelingapp.core.response.user.UserListResponse


interface IUserRepository {
    suspend fun getAllUsers(token: String, userRequest: UserRequest) : UserListResponse
    suspend fun syncAndPersistUsers(token: String, userRequest: UserRequest)
}