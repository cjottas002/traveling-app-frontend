package org.example.travelingapp.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.example.travelingapp.data.local.entities.PendingOperationEntity

@Dao
interface PendingOperationDao {

    @Query("SELECT * FROM pending_operations ORDER BY createdAt ASC")
    suspend fun getAll(): List<PendingOperationEntity>

    @Insert
    suspend fun insert(operation: PendingOperationEntity)

    @Query("DELETE FROM pending_operations WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM pending_operations")
    suspend fun count(): Int
}
