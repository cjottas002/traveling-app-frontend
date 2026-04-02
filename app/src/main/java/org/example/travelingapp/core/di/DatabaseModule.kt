package org.example.travelingapp.core.di

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.example.travelingapp.R
import org.example.travelingapp.core.security.PasswordHasher
import org.example.travelingapp.data.local.AppDatabase
import org.example.travelingapp.data.local.daos.DestinationDao
import org.example.travelingapp.data.local.daos.PendingOperationDao
import org.example.travelingapp.data.local.daos.TransportDao
import org.example.travelingapp.data.local.daos.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "traveling.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    seedTransports(db)
                    seedUsers(db)
                    seedDestinations(db)
                }
            })
            .fallbackToDestructiveMigration(true)
            .build()
    }

    private fun seedTransports(db: SupportSQLiteDatabase) {
        val transports = listOf(
            "AirPlane"     to R.drawable.pestania1_airplain     to "$11/day",
            "Bus"          to R.drawable.pestania1_bus          to "$14/day",
            "Classic Car"  to R.drawable.pestania1_classiccar   to "$34/day",
            "Electric Car" to R.drawable.pestania1_electriccar  to "$45/day",
            "Flying Car"   to R.drawable.pestania1_flyingcar    to "$500/day",
            "MotorHome"    to R.drawable.pestania1_motorhome    to "$23/day",
            "PickUp Car"   to R.drawable.pestania1_pickupcar    to "$10/day",
            "Sport Car"    to R.drawable.pestania1_sportcart    to "$55/day"
        )

        transports.forEach { (nameAndRes, price) ->
            val (name, imageRes) = nameAndRes
            db.insert("transports", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
                put("name", name)
                put("imageRes", imageRes)
                put("price", price)
            })
        }
    }

    private fun seedUsers(db: SupportSQLiteDatabase) {
        db.insert("users", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
            put("id", "1234567A")
            put("username", "admin")
            put("email", "admin@test.com")
            put("updateAt", System.currentTimeMillis())
            put("passwordHash", PasswordHasher.hash("Admin123!"))
            put("role", "Admin")
        })
        db.insert("users", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
            put("id", "1234567B")
            put("username", "customer1")
            put("email", "customer1@test.com")
            put("updateAt", System.currentTimeMillis())
            put("passwordHash", PasswordHasher.hash("Customer1!"))
            put("role", "Customer")
        })
    }

    private fun seedDestinations(db: SupportSQLiteDatabase) {
        db.insert("destinations", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
            put("id", "a1b2c3d4-0001-0001-0001-000000000001")
            put("name", "Playa de Cancún")
            put("description", "Hermosas playas de arena blanca y aguas turquesa en la Riviera Maya")
            put("country", "México")
            put("imageUrl", "local:destination_cancun")
            put("category", "beach")
            put("createdBy", "1234567A")
            put("createdAt", System.currentTimeMillis())
            put("updatedAt", System.currentTimeMillis())
        })
        db.insert("destinations", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
            put("id", "a1b2c3d4-0001-0001-0001-000000000002")
            put("name", "Alpes Suizos")
            put("description", "Montañas nevadas espectaculares con vistas panorámicas")
            put("country", "Suiza")
            put("imageUrl", "local:destination_alps")
            put("category", "mountain")
            put("createdBy", "1234567A")
            put("createdAt", System.currentTimeMillis())
            put("updatedAt", System.currentTimeMillis())
        })
    }

    @Provides
    fun provideTransportDao(db: AppDatabase): TransportDao {
        return db.transportDao()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    fun provideDestinationDao(db: AppDatabase): DestinationDao {
        return db.destinationDao()
    }

    @Provides
    fun providePendingOperationDao(db: AppDatabase): PendingOperationDao {
        return db.pendingOperationDao()
    }
}
