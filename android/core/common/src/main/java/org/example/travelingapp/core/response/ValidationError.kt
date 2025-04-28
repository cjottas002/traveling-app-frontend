package org.example.travelingapp.core.response


data class ValidationError(
    val fields: List<String>,
    val message: String
)