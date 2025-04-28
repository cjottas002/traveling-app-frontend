package org.example.travelingapp.data.mapper

import org.example.travelingapp.data.local.entities.TransportEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class TransportMapperTest {

    @Test
    fun toTransport_mapsEntityIntoDomainModel() {
        val entity = TransportEntity(
            id = 1,
            name = "Bus",
            imageRes = 123,
            price = "$10/day"
        )

        val transport = entity.toTransport()

        assertEquals("Bus", transport.name)
        assertEquals(123, transport.imageRes)
        assertEquals("$10/day", transport.price)
    }
}
