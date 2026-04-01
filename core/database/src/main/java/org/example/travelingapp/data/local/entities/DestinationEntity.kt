package org.example.travelingapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "destinations")
data class DestinationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val country: String,
    val imageUrl: String,
    val category: String,
    val createdBy: String,
    val createdAt: Long,
    val updatedAt: Long
)
