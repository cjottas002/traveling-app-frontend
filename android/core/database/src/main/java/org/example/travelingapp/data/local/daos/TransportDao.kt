package org.example.travelingapp.data.local.daos


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.example.travelingapp.data.local.entities.TransportEntity

@Dao
interface TransportDao {

    @Query("SELECT * FROM transports")
    fun getAllTransports(): Flow<List<TransportEntity>>

    @Query("SELECT * FROM transports")
    suspend fun getAllTransportsOnce(): List<TransportEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transports: List<TransportEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transport: TransportEntity)

    @Update
    suspend fun update(transport: TransportEntity)

    @Delete
    suspend fun delete(transport: TransportEntity)

    @Query("DELETE FROM transports")
    suspend fun deleteAll()

    @Query("SELECT * FROM transports WHERE id = :id")
    suspend fun getTransportById(id: String): TransportEntity?
}