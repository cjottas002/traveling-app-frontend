package org.example.travelingapp.domain.repository

import org.example.travelingapp.core.response.login.LoginResponse
import org.example.travelingapp.core.response.register.RegisterResponse


interface IAccountRepository {
    suspend fun remoteRegister(username: String, pass: String): RegisterResponse
    suspend fun remoteLogin(username: String, pass: String): LoginResponse

    suspend fun localLogin(username: String, pass: String) : LoginResponse
    suspend fun localRegister(username: String, pass: String) : RegisterResponse
}