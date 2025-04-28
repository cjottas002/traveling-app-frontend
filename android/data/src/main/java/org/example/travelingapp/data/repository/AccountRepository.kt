package org.example.travelingapp.data.repository


import org.example.travelingapp.core.request.login.LoginRequest
import org.example.travelingapp.core.request.register.RegisterRequest
import org.example.travelingapp.core.response.login.LoginResponse
import org.example.travelingapp.core.response.register.RegisterResponse
import org.example.travelingapp.data.local.daos.UserDao
import org.example.travelingapp.domain.repository.IAccountRepository
import org.example.travelingapp.data.remote.services.IAccountService
import javax.inject.Inject

class AccountRepository @Inject constructor(private val accountService: IAccountService, private val userDao: UserDao) : IAccountRepository {

    override suspend fun remoteRegister(username: String, pass: String): RegisterResponse {
        return accountService.register(RegisterRequest(username, pass)).body()!!
    }

    override suspend fun remoteLogin(username: String, pass: String): LoginResponse {
        return accountService.login(LoginRequest(username, pass)).body()!!
    }

    override suspend fun localLogin(username: String, pass: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun localRegister(username: String, pass: String): RegisterResponse {
        TODO("Not yet implemented")
    }
}