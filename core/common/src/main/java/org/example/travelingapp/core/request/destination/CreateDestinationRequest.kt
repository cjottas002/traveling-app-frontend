package org.example.travelingapp.core.request.destination

data class CreateDestinationRequest(
    val name: String,
    val description: String,
    val country: String,
    val imageUrl: String,
    val category: String
)
