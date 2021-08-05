package com.atdev.githubproject.components.di

import com.atdev.githubproject.MainRepository
import com.atdev.githubproject.search.api.ApiService
import com.atdev.githubproject.profile.auth.data.LoginDataSource
import com.atdev.githubproject.profile.auth.data.LoginRepository
import com.atdev.githubproject.collection.room.RepositoryDao
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

    @Singleton
    @Provides
    fun provideDataSource(
    ) = LoginDataSource()

    @Singleton
    @Provides
    fun provideLoginRepository(
        loginDataSource: LoginDataSource,
        apiService: ApiService
    ) = LoginRepository(apiService,loginDataSource)
}