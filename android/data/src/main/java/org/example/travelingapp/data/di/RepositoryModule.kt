package org.example.travelingapp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.example.travelingapp.data.repository.AccountRepository
import org.example.travelingapp.data.repository.HotelRepository
import org.example.travelingapp.data.repository.TransportRepository
import org.example.travelingapp.data.repository.UserRepository
import org.example.travelingapp.domain.repository.IAccountRepository
import org.example.travelingapp.domain.repository.IHotelRepository
import org.example.travelingapp.domain.repository.ITransportRepository
import org.example.travelingapp.domain.repository.IUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccountRepository(impl: AccountRepository): IAccountRepository

    @Binds
    @Singleton
    abstract fun bindHotelRepository(impl: HotelRepository): IHotelRepository

    @Binds
    @Singleton
    abstract fun bindTransportRepository(impl: TransportRepository): ITransportRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepository): IUserRepository
}
