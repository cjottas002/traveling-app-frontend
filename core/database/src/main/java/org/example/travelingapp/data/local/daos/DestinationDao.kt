package org.example.travelingapp.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.travelingapp.data.local.entities.DestinationEntity

@Dao
interface DestinationDao {

    @Query("SELECT * FROM destinations ORDER BY name ASC")
    fun getAll(): Flow<List<DestinationEntity>>

    @Query("SELECT * FROM destinations WHERE category = :category ORDER BY name ASC")
    fun getByCategory(category: String): Flow<List<DestinationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(destinations: List<DestinationEntity>)

    @Query("DELETE FROM destinations")
    suspend fun deleteAll()

    @Query("DELETE FROM destinations WHERE imageUrl NOT LIKE 'local:%'")
    suspend fun deleteRemoteOnly()

    @Query("SELECT * FROM destinations WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): DestinationEntity?

    @Query("DELETE FROM destinations WHERE id = :id")
    suspend fun deleteById(id: String)
}
