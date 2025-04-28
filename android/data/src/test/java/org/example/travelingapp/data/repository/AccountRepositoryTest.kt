package org.example.travelingapp.data.repository

import kotlinx.coroutines.runBlocking
import org.example.travelingapp.core.request.login.LoginRequest
import org.example.travelingapp.core.request.register.RegisterRequest
import org.example.travelingapp.core.response.login.LoginResponse
import org.example.travelingapp.core.response.login.dtos.LoginDto
import org.example.travelingapp.core.response.register.RegisterResponse
import org.example.travelingapp.core.response.register.dtos.RegisterDto
import org.example.travelingapp.data.local.daos.UserDao
import org.example.travelingapp.data.local.entities.UserEntity
import org.example.travelingapp.data.remote.services.IAccountService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.Response

class AccountRepositoryTest {

    @Test
    fun remoteRegister_returnsBodyFromService() = runBlocking {
        val expected = RegisterResponse().apply { data = RegisterDto(isRegistered = true) }
        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(expected),
                loginResponse = Response.success(LoginResponse())
            ),
            userDao = NoOpUserDao()
        )

        val result = repository.remoteRegister("john", "1234")

        assertTrue(result.success)
        assertEquals(true, result.data?.isRegistered)
    }

    @Test
    fun remoteLogin_returnsBodyFromService() = runBlocking {
        val expected = LoginResponse().apply { data = LoginDto(userId = "u-1", token = "token-1") }
        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(RegisterResponse()),
                loginResponse = Response.success(expected)
            ),
            userDao = NoOpUserDao()
        )

        val result = repository.remoteLogin("john", "1234")

        assertTrue(result.success)
        assertEquals("u-1", result.data?.userId)
        assertEquals("token-1", result.data?.token)
    }

    @Test
    fun localLogin_throwsNotImplementedError() = runBlocking {
        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(RegisterResponse()),
                loginResponse = Response.success(LoginResponse())
            ),
            userDao = NoOpUserDao()
        )

        try {
            repository.localLogin("john", "1234")
            fail("Expected NotImplementedError")
        } catch (_: NotImplementedError) {
            // Expected path while local auth is pending implementation.
        }
    }

    @Test
    fun localRegister_throwsNotImplementedError() = runBlocking {
        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(RegisterResponse()),
                loginResponse = Response.success(LoginResponse())
            ),
            userDao = NoOpUserDao()
        )

        try {
            repository.localRegister("john", "1234")
            fail("Expected NotImplementedError")
        } catch (_: NotImplementedError) {
            // Expected path while local auth is pending implementation.
        }
    }

    private class FakeAccountService(
        private val registerResponse: Response<RegisterResponse>,
        private val loginResponse: Response<LoginResponse>
    ) : IAccountService {
        override suspend fun login(request: LoginRequest): Response<LoginResponse> = loginResponse
        override suspend fun register(request: RegisterRequest): Response<RegisterResponse> = registerResponse
    }

    private class NoOpUserDao : UserDao {
        override fun getAllUsers() = kotlinx.coroutines.flow.emptyFlow<List<UserEntity>>()
        override suspend fun getUserById(id: Int): UserEntity? = null
        override suspend fun insertAll(users: List<UserEntity>) = Unit
        override suspend fun insert(user: UserEntity) = Unit
        override suspend fun update(user: UserEntity) = Unit
        override suspend fun delete(user: UserEntity) = Unit
        override suspend fun deleteAll() = Unit
    }
}
