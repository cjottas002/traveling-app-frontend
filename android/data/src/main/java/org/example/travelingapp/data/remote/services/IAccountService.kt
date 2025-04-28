package org.example.travelingapp.data.remote.services


import org.example.travelingapp.core.request.login.LoginRequest
import org.example.travelingapp.core.request.register.RegisterRequest
import org.example.travelingapp.core.response.login.LoginResponse
import org.example.travelingapp.core.response.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IAccountService {
    @POST("api/account/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/account/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}
