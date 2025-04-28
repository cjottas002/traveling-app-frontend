package org.example.travelingapp.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.travelingapp.R
import org.example.travelingapp.data.local.AppDatabase
import org.example.travelingapp.data.local.daos.TransportDao
import org.example.travelingapp.data.local.daos.UserDao
import org.example.travelingapp.data.local.entities.TransportEntity
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

                    CoroutineScope(Dispatchers.IO).launch {
                        val database = Room.databaseBuilder(
                            appContext,
                            AppDatabase::class.java,
                            "traveling.db"
                        ).build()

                        val dao = database.transportDao()

                        val initial = listOf(
                            TransportEntity(name = "AirPlane",     imageRes = R.drawable.pestania1_airplain,     price = "$11/day"),
                            TransportEntity(name = "Bus",          imageRes = R.drawable.pestania1_bus,          price = "$14/day"),
                            TransportEntity(name = "Classic Car",  imageRes = R.drawable.pestania1_classiccar,   price = "$34/day"),
                            TransportEntity(name = "Electric Car", imageRes = R.drawable.pestania1_electriccar,  price = "$45/day"),
                            TransportEntity(name = "Flying Car",   imageRes = R.drawable.pestania1_flyingcar,    price = "$500/day"),
                            TransportEntity(name = "MotorHome",    imageRes = R.drawable.pestania1_motorhome,    price = "$23/day"),
                            TransportEntity(name = "PickUp Car",   imageRes = R.drawable.pestania1_pickupcar,    price = "$10/day"),
                            TransportEntity(name = "Sport Car",    imageRes = R.drawable.pestania1_sportcart,    price = "$55/day")
                        )

                        dao.insertAll(initial)
                    }
                }
            })
            .fallbackToDestructiveMigration(true)
            .build()
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
