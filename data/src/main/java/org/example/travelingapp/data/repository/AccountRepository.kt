package org.example.travelingapp.data.repository

import org.example.travelingapp.core.request.login.LoginRequest
import org.example.travelingapp.core.request.register.RegisterRequest
import org.example.travelingapp.core.response.ValidationError
import org.example.travelingapp.core.response.login.LoginResponse
import org.example.travelingapp.core.response.login.dtos.LoginDto
import org.example.travelingapp.core.response.register.RegisterResponse
import org.example.travelingapp.core.security.PasswordHasher
import org.example.travelingapp.data.local.daos.UserDao
import org.example.travelingapp.domain.repository.IAccountRepository
import org.example.travelingapp.data.remote.services.IAccountService
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountService: IAccountService,
    private val userDao: UserDao
) : IAccountRepository {

    override suspend fun remoteRegister(username: String, pass: String): RegisterResponse {
        return accountService.register(RegisterRequest(username, pass)).body()!!
    }

    override suspend fun remoteLogin(username: String, pass: String): LoginResponse {
        val response = accountService.login(LoginRequest(username, pass)).body()!!

        if (response.success && response.data?.token != null) {
            cacheCredentials(username, pass)
        }

        return response
    }

    override suspend fun localLogin(username: String, pass: String): LoginResponse {
        val cached = userDao.getUserByUsername(username)
            ?: return LoginResponse(
                errors = listOf(ValidationError(fields = listOf("username"), message = "No cached credentials for this user"))
            )

        val hash = cached.passwordHash
            ?: return LoginResponse(
                errors = listOf(ValidationError(fields = listOf("username"), message = "No offline credentials available. Please log in online first."))
            )

        if (!PasswordHasher.verify(pass, hash)) {
            return LoginResponse(
                errors = listOf(ValidationError(fields = listOf("password"), message = "Invalid username or password"))
            )
        }

        return LoginResponse(
            data = LoginDto(userId = cached.id, token = "offline-session"),
            count = 1
        )
    }

    override suspend fun localRegister(username: String, pass: String): RegisterResponse {
        return RegisterResponse(
            errors = listOf(ValidationError(fields = emptyList(), message = "Registration requires an internet connection"))
        )
    }

    private suspend fun cacheCredentials(username: String, password: String) {
        val existing = userDao.getUserByUsername(username) ?: return
        val updated = existing.copy(passwordHash = PasswordHasher.hash(password))
        userDao.update(updated)
    }
}
