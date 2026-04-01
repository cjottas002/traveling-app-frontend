package org.example.travelingapp.integration

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.example.travelingapp.core.request.user.UserRequest
import org.example.travelingapp.core.response.user.UserListResponse
import org.example.travelingapp.core.response.user.dtos.UserDto
import org.example.travelingapp.data.local.AppDatabase
import org.example.travelingapp.data.local.entities.TransportEntity
import org.example.travelingapp.data.mapper.toEntities
import org.example.travelingapp.data.repository.TransportRepository
import org.example.travelingapp.data.repository.UserRepository
import org.example.travelingapp.data.remote.services.IUserService
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class RepositoryIntegrationTest {

    private lateinit var database: AppDatabase
    private lateinit var transportRepository: TransportRepository
    private lateinit var userRepository: UserRepository
    private lateinit var fakeUserService: FakeUserService

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        fakeUserService = FakeUserService()
        transportRepository = TransportRepository(database.transportDao())
        userRepository = UserRepository(fakeUserService, database.userDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun transportRepository_readsItemsStoredInDao() = runBlocking {
        database.transportDao().insertAll(
            listOf(
                TransportEntity(name = "Bus", imageRes = 1, price = "\$10/day"),
                TransportEntity(name = "Car", imageRes = 2, price = "\$50/day")
            )
        )

        val result = transportRepository.getTransports().first()

        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Bus" })
        assertTrue(result.any { it.name == "Car" })
    }

    @Test
    fun userRepository_fetchAndPersistUsers_withMappedRequest() = runBlocking {
        val request = UserRequest().apply {
            pageIndex = 2
            pageSize = 25
            orderBy = "name"
            orderByAsc = false
        }

        val response = userRepository.getAllUsers(token = "Bearer token-1", userRequest = request)
        userRepository.insertUser(response.toEntities())

        val stored = database.userDao().getAllUsers().first()

        assertEquals("Bearer token-1", fakeUserService.receivedToken)
        assertEquals("2", fakeUserService.receivedQuery["pageIndex"])
        assertEquals("25", fakeUserService.receivedQuery["pageSize"])
        assertEquals("name", fakeUserService.receivedQuery["orderBy"])
        assertEquals("false", fakeUserService.receivedQuery["orderByAsc"])
        assertFalse(stored.isEmpty())
        assertEquals("user-1", stored.first().id)
        assertEquals("John", stored.first().username)
    }

    private class FakeUserService : IUserService {
        var receivedToken: String? = null
        var receivedQuery: Map<String, String> = emptyMap()

        override suspend fun getAllUsers(
            token: String,
            query: Map<String, String>
        ): Response<UserListResponse> {
            receivedToken = token
            receivedQuery = query
            return Response.success(
                UserListResponse().apply {
                    data = listOf(
                        UserDto(
                            id = "user-1",
                            username = "John",
                            email = "john@mail.com",
                            updatedAt = 12L
                        )
                    )
                }
            )
        }
    }
}
