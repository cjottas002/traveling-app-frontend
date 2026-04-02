package org.example.travelingapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_operations")
data class PendingOperationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val operationType: String,   // CREATE, UPDATE, DELETE
    val entityType: String,       // Destination, etc.
    val entityId: String,
    val payload: String,          // JSON of the request body
    val createdAt: Long = System.currentTimeMillis()
)
