package org.example.travelingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.travelingapp.data.local.daos.TransportDao
import org.example.travelingapp.data.local.daos.UserDao
import org.example.travelingapp.data.local.entities.TransportEntity
import org.example.travelingapp.data.local.entities.UserEntity

@Database(
    entities = [TransportEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transportDao(): TransportDao
    abstract fun userDao(): UserDao
}