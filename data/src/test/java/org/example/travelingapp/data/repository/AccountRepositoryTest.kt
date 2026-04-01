package org.example.travelingapp.data.repository

import kotlinx.coroutines.runBlocking
import org.example.travelingapp.core.request.login.LoginRequest
import org.example.travelingapp.core.request.register.RegisterRequest
import org.example.travelingapp.core.response.login.LoginResponse
import org.example.travelingapp.core.response.login.dtos.LoginDto
import org.example.travelingapp.core.response.register.RegisterResponse
import org.example.travelingapp.core.response.register.dtos.RegisterDto
import org.example.travelingapp.core.security.PasswordHasher
import org.example.travelingapp.data.local.daos.UserDao
import org.example.travelingapp.data.local.entities.UserEntity
import org.example.travelingapp.data.remote.services.IAccountService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class AccountRepositoryTest {

    @Test
    fun remoteRegister_returnsBodyFromService() = runBlocking {
        val expected = RegisterResponse(data = RegisterDto(isRegistered = true), count = 1)
        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(expected),
                loginResponse = Response.success(LoginResponse())
            ),
            userDao = InMemoryUserDao()
        )

        val result = repository.remoteRegister("john", "1234")

        assertTrue(result.success)
        assertEquals(true, result.data?.isRegistered)
    }

    @Test
    fun remoteLogin_returnsBodyAndCachesCredentials() = runBlocking {
        val dao = InMemoryUserDao()
        dao.insert(UserEntity(id = "1", username = "admin", email = "admin@test.com", updateAt = 0))

        val expected = LoginResponse(data = LoginDto(userId = "1", token = "jwt-token"), count = 1)
        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(RegisterResponse()),
                loginResponse = Response.success(expected)
            ),
            userDao = dao
        )

        val result = repository.remoteLogin("admin", "Admin123!")

        assertTrue(result.success)
        assertEquals("jwt-token", result.data?.token)

        val cached = dao.getUserByUsername("admin")
        assertTrue(cached?.passwordHash != null)
        assertTrue(PasswordHasher.verify("Admin123!", cached!!.passwordHash!!))
    }

    @Test
    fun localLogin_returnsSuccessWhenCredentialsCached() = runBlocking {
        val dao = InMemoryUserDao()
        val hash = PasswordHasher.hash("Admin123!")
        dao.insert(UserEntity(id = "1", username = "admin", email = "admin@test.com", updateAt = 0, passwordHash = hash))

        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(RegisterResponse()),
                loginResponse = Response.success(LoginResponse())
            ),
            userDao = dao
        )

        val result = repository.localLogin("admin", "Admin123!")

        assertTrue(result.success)
        assertEquals("1", result.data?.userId)
    }

    @Test
    fun localLogin_returnsErrorWhenUserNotCached() = runBlocking {
        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(RegisterResponse()),
                loginResponse = Response.success(LoginResponse())
            ),
            userDao = InMemoryUserDao()
        )

        val result = repository.localLogin("unknown", "pass")

        assertFalse(result.success)
        assertTrue(result.errors.isNotEmpty())
    }

    @Test
    fun localLogin_returnsErrorWhenPasswordWrong() = runBlocking {
        val dao = InMemoryUserDao()
        val hash = PasswordHasher.hash("CorrectPass")
        dao.insert(UserEntity(id = "1", username = "admin", email = "admin@test.com", updateAt = 0, passwordHash = hash))

        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(RegisterResponse()),
                loginResponse = Response.success(LoginResponse())
            ),
            userDao = dao
        )

        val result = repository.localLogin("admin", "WrongPass")

        assertFalse(result.success)
    }

    @Test
    fun localRegister_returnsErrorRequiringInternet() = runBlocking {
        val repository = AccountRepository(
            accountService = FakeAccountService(
                registerResponse = Response.success(RegisterResponse()),
                loginResponse = Response.success(LoginResponse())
            ),
            userDao = InMemoryUserDao()
        )

        val result = repository.localRegister("john", "1234")

        assertFalse(result.success)
        assertTrue(result.errors.any { it.message.contains("internet", ignoreCase = true) })
    }

    private class FakeAccountService(
        private val registerResponse: Response<RegisterResponse>,
        private val loginResponse: Response<LoginResponse>
    ) : IAccountService {
        override suspend fun login(request: LoginRequest): Response<LoginResponse> = loginResponse
        override suspend fun register(request: RegisterRequest): Response<RegisterResponse> = registerResponse
    }

    private class InMemoryUserDao : UserDao {
        private val users = mutableListOf<UserEntity>()

        override fun getAllUsers() = kotlinx.coroutines.flow.flowOf(users.toList())
        override suspend fun getUserById(id: Int): UserEntity? = users.find { it.id == id.toString() }
        override suspend fun insertAll(users: List<UserEntity>) { this.users.addAll(users) }
        override suspend fun insert(user: UserEntity) { users.removeAll { it.id == user.id }; users.add(user) }
        override suspend fun update(user: UserEntity) { users.replaceAll { if (it.id == user.id) user else it } }
        override suspend fun delete(user: UserEntity) { users.removeAll { it.id == user.id } }
        override suspend fun deleteAll() { users.clear() }
        override suspend fun getUserByUsername(username: String): UserEntity? = users.find { it.username == username }
    }
}
