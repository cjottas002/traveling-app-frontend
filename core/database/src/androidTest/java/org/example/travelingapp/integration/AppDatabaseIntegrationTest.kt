package org.example.travelingapp.integration

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.example.travelingapp.data.local.AppDatabase
import org.example.travelingapp.data.local.daos.TransportDao
import org.example.travelingapp.data.local.daos.UserDao
import org.example.travelingapp.data.local.entities.TransportEntity
import org.example.travelingapp.data.local.entities.UserEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseIntegrationTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var transportDao: TransportDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = database.userDao()
        transportDao = database.transportDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun userDao_insertReadAndDeleteAll() = runBlocking {
        val user = UserEntity(
            id = "u-1",
            username = "john",
            email = "john@mail.com",
            updateAt = 1L
        )

        userDao.insert(user)
        val storedUsers = userDao.getAllUsers().first()
        assertEquals(1, storedUsers.size)
        assertEquals("u-1", storedUsers.first().id)

        userDao.deleteAll()
        assertTrue(userDao.getAllUsers().first().isEmpty())
    }

    @Test
    fun transportDao_insertAllReadAndUpdate() = runBlocking {
        val transports = listOf(
            TransportEntity(name = "Bus", imageRes = 1, price = "\$10/day"),
            TransportEntity(name = "Train", imageRes = 2, price = "\$20/day")
        )

        transportDao.insertAll(transports)
        val stored = transportDao.getAllTransportsOnce()
        assertEquals(2, stored.size)
        assertTrue(stored.any { it.name == "Bus" })

        val bus = stored.first { it.name == "Bus" }
        val updatedBus = bus.copy(price = "\$15/day")
        transportDao.update(updatedBus)

        val refreshed = transportDao.getAllTransportsOnce().first { it.id == updatedBus.id }
        assertEquals("\$15/day", refreshed.price)
    }
}
