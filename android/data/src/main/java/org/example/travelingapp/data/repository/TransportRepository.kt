package org.example.travelingapp.data.repository


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.travelingapp.data.local.daos.TransportDao
import org.example.travelingapp.data.mapper.toTransport
import org.example.travelingapp.domain.entities.Transport
import org.example.travelingapp.domain.repository.ITransportRepository
import javax.inject.Inject

class TransportRepository @Inject constructor(private val transportDao: TransportDao) : ITransportRepository {
    override fun getTransports(): Flow<List<Transport>> {
        return transportDao.getAllTransports().map { entities ->
            entities.map { it.toTransport() }
        }
    }
}