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
    }

    @Provides
    fun provideTransportDao(db: AppDatabase): TransportDao {
        return db.transportDao()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }
}
