package com.atdev.githubproject.di

import android.app.Application
import com.atdev.githubproject.room.RepositoryDatabase
import com.atdev.githubproject.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): ApiService = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(
            GsonConverterFactory.create()).build()
        .create(ApiService::class.java)


    @Singleton
    @Provides
    fun provideDatabase(application: Application) = RepositoryDatabase.getDatabase(application)


    @Singleton
    @Provides
    fun provideDao(db: RepositoryDatabase) = db.repositoryDao()

}