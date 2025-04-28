package org.example.travelingapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.travelingapp.domain.entities.Transport

interface ITransportRepository {
    fun getTransports(): Flow<List<Transport>>
}