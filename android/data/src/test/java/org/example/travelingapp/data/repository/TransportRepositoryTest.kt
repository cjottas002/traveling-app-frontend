package org.example.travelingapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.example.travelingapp.data.local.daos.TransportDao
import org.example.travelingapp.data.local.entities.TransportEntity
import org.example.travelingapp.domain.entities.Transport
import org.junit.Assert.assertEquals
import org.junit.Test

class TransportRepositoryTest {

    @Test
    fun getTransports_returnsMappedDomainModels() = runBlocking {
        val entities = listOf(
            TransportEntity(name = "Bus", imageRes = 1, price = "10"),
            TransportEntity(name = "Train", imageRes = 2, price = "20")
        )
        val expected = listOf(
            Transport(name = "Bus", imageRes = 1, price = "10"),
            Transport(name = "Train", imageRes = 2, price = "20")
        )
        val dao = FakeTransportDao(flow = flowOf(entities))
        val repository = TransportRepository(dao)

        val result = repository.getTransports().first()

        assertEquals(expected, result)
    }

    private class FakeTransportDao(
        val flow: Flow<List<TransportEntity>>
    ) : TransportDao {
        override fun getAllTransports(): Flow<List<TransportEntity>> = flow
        override suspend fun getAllTransportsOnce(): List<TransportEntity> = flow.first()
        override suspend fun insertAll(transports: List<TransportEntity>) = Unit
        override suspend fun insert(transport: TransportEntity) = Unit
        override suspend fun update(transport: TransportEntity) = Unit
        override suspend fun delete(transport: TransportEntity) = Unit
        override suspend fun deleteAll() = Unit
        override suspend fun getTransportById(id: String): TransportEntity? = null
    }
}
