package org.example.travelingapp.data.remote.services

import org.example.travelingapp.core.response.user.UserListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface IUserService {

    @GET("api/user/getAllUsers")
    suspend fun getAllUsers(
        @Header("Authorization") token: String,
        @QueryMap query: Map<String, String>
    ): Response<UserListResponse>
}