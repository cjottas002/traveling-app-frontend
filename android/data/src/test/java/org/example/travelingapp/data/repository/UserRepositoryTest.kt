package org.example.travelingapp.data.repository

import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.example.travelingapp.core.request.user.UserRequest
import org.example.travelingapp.core.response.user.UserListResponse
import org.example.travelingapp.core.response.user.dtos.UserDto
import org.example.travelingapp.data.local.daos.UserDao
import org.example.travelingapp.data.local.entities.UserEntity
import org.example.travelingapp.data.remote.services.IUserService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import retrofit2.Response

class UserRepositoryTest {

    @Test
    fun getAllUsers_callsServiceWithMappedQueryAndReturnsBody() = runBlocking {
        val expectedResponse = UserListResponse().apply {
            data = listOf(
                UserDto(
                    id = "u-1",
                    name = "John",
                    email = "john@mail.com",
                    password = "123",
                    updatedAt = 5L
                )
            )
        }
        val fakeService = FakeUserService(Response.success(expectedResponse))
        val repository = UserRepository(fakeService, RecordingUserDao())
        val request = UserRequest().apply {
            pageIndex = 3
            pageSize = 50
            orderBy = "name"
            orderByAsc = false
        }

        val result = repository.getAllUsers(token = "Bearer abc", userRequest = request)

        assertSame(expectedResponse, result)
        assertEquals("Bearer abc", fakeService.receivedToken)
        assertEquals("3", fakeService.receivedQuery["pageIndex"])
        assertEquals("50", fakeService.receivedQuery["pageSize"])
        assertEquals("name", fakeService.receivedQuery["orderBy"])
        assertEquals("false", fakeService.receivedQuery["orderByAsc"])
    }

    @Test
    fun insertUser_delegatesToDao() = runBlocking {
        val dao = RecordingUserDao()
        val repository = UserRepository(
            userService = FakeUserService(Response.success(UserListResponse())),
            userDao = dao
        )
        val users = listOf(
            UserEntity("id-1", "john", "john@mail.com", "123", 1L),
            UserEntity("id-2", "mary", "mary@mail.com", "abc", 2L)
        )

        repository.insertUser(users)

        assertEquals(users, dao.insertedUsers)
    }

    private class FakeUserService(
        private val response: Response<UserListResponse>
    ) : IUserService {
        var receivedToken: String? = null
        var receivedQuery: Map<String, String> = emptyMap()

        override suspend fun getAllUsers(
            token: String,
            query: Map<String, String>
        ): Response<UserListResponse> {
            receivedToken = token
            receivedQuery = query
            return response
        }
    }

    private class RecordingUserDao : UserDao {
        var insertedUsers: List<UserEntity>? = null

        override fun getAllUsers() = emptyFlow<List<UserEntity>>()
        override suspend fun getUserById(id: Int): UserEntity? = null

        override suspend fun insertAll(users: List<UserEntity>) {
            insertedUsers = users
        }

        override suspend fun insert(user: UserEntity) = Unit
        override suspend fun update(user: UserEntity) = Unit
        override suspend fun delete(user: UserEntity) = Unit
        override suspend fun deleteAll() = Unit
    }
}
