package org.example.travelingapp.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.example.travelingapp.data.remote.services.IUserService
import org.example.travelingapp.core.network.AndroidNetworkChecker
import org.example.travelingapp.core.network.NetworkExecutor
import org.example.travelingapp.core.network.interfaces.INetworkChecker
import org.example.travelingapp.data.local.daos.DestinationDao
import org.example.travelingapp.data.remote.services.IAccountService
import org.example.travelingapp.data.remote.services.IDestinationService
import org.example.travelingapp.data.remote.services.IHotelService
import org.example.travelingapp.data.local.daos.PendingOperationDao
import org.example.travelingapp.data.repository.DestinationRepository
import org.example.travelingapp.data.sync.SyncManager
import org.example.travelingapp.domain.repository.IDestinationRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL_BACKEND = "http://192.168.1.66:5090/"
    private const val BASE_URL_MOCK = "https://01394d44-8918-4a1d-8059-629c50c25e87.mock.pstmn.io/"

    @Provides
    @Singleton
    @Named("BackendRetrofit")
    fun provideBackendRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_BACKEND)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("MockRetrofit")
    fun provideMockRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_MOCK)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesHotelService(@Named("MockRetrofit") retrofit: Retrofit): IHotelService {
        return retrofit.create(IHotelService::class.java)
    }

    @Provides
    @Singleton
    fun providesLoginService(@Named("BackendRetrofit") retrofit: Retrofit): IAccountService {
        return retrofit.create(IAccountService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(@Named("BackendRetrofit") retrofit: Retrofit): IUserService {
        return retrofit.create(IUserService::class.java)
    }

    @Provides
    @Singleton
    fun provideDestinationService(@Named("BackendRetrofit") retrofit: Retrofit): IDestinationService {
        return retrofit.create(IDestinationService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context): INetworkChecker {
        return AndroidNetworkChecker(context)
    }

    @Provides
    @Singleton
    fun provideNetworkExecutor(networkChecker: INetworkChecker): NetworkExecutor {
        return NetworkExecutor(networkChecker)
    }

    @Provides
    @Singleton
    fun provideSyncManager(
        pendingOperationDao: PendingOperationDao,
        @ApplicationContext context: Context
    ): SyncManager {
        return SyncManager(pendingOperationDao, context)
    }

    @Provides
    @Singleton
    fun provideDestinationRepository(
        destinationService: IDestinationService,
        destinationDao: DestinationDao,
        syncManager: SyncManager
    ): IDestinationRepository {
        return DestinationRepository(destinationService, destinationDao, syncManager)
    }
}