package org.example.travelingapp.data.mapper

import org.example.travelingapp.data.local.entities.TransportEntity
import org.example.travelingapp.domain.entities.Transport

fun TransportEntity.toTransport() : Transport {
    return Transport(
        name = this.name,
        imageRes = this.imageRes,
        price = this.price
    )
}