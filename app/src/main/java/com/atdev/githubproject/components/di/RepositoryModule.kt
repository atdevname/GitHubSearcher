package com.atdev.githubproject.components.di

import com.atdev.githubproject.MainRepository
import com.atdev.githubproject.collection.room.RepositoryDao
import com.atdev.githubproject.search.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    
    @Singleton
    @Provides
    fun provideRepository(
        repositoryDao: RepositoryDao,
        apiService: ApiService
    ) = MainRepository(repositoryDao, apiService)
}