package org.example.travelingapp.data.repository


import org.example.travelingapp.core.extensions.toQueryMap
import org.example.travelingapp.core.request.user.UserRequest
import org.example.travelingapp.core.response.user.UserListResponse
import org.example.travelingapp.data.remote.services.IUserService
import org.example.travelingapp.data.local.daos.UserDao
import org.example.travelingapp.data.mapper.toEntities
import org.example.travelingapp.data.local.entities.UserEntity
import org.example.travelingapp.domain.repository.IUserRepository
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService : IUserService, private val userDao : UserDao) : IUserRepository  {

    override suspend fun getAllUsers(token: String, userRequest: UserRequest): UserListResponse {
        return userService.getAllUsers(token, userRequest.toQueryMap()).body()!!
    }

    suspend fun insertUser(users: List<UserEntity>) {
        userDao.insertAll(users)
    }

    override suspend fun syncAndPersistUsers(token: String, userRequest: UserRequest) {
        val response = getAllUsers(token, userRequest)
        if (response.success && response.data.isNotEmpty()) {
            userDao.insertAll(response.toEntities())
        }
    }
}