package com.example.colorpalettesapp.di

import com.backendless.Backendless
import com.backendless.Persistence
import com.example.colorpalettesapp.data.repository.BackendlessDataSourceImpl
import com.example.colorpalettesapp.data.repository.RepositoryImpl
import com.example.colorpalettesapp.domain.repository.BackendlessDataSource
import com.example.colorpalettesapp.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideBackendless(): Persistence {
        return Backendless.Data
    }

    @Provides
    @Singleton
    fun provideBackendlessDataSource(backendless: Persistence): BackendlessDataSource {
        return BackendlessDataSourceImpl(backendless = backendless)
    }

    @Provides
    @Singleton
    fun provideRepository(backendlessDataSource: BackendlessDataSource): Repository {
        return RepositoryImpl(backendlessDataSource = backendlessDataSource)
    }

}