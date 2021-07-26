package com.atdev.githubproject.di

import android.app.Application
import com.atdev.githubproject.activity.ConnectionChecker
import com.atdev.githubproject.helpers.MainRepository
import com.atdev.githubproject.room.RepositoryDatabase
import com.atdev.githubproject.retrofit.ApiService
import com.atdev.githubproject.room.RepositoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CommonModule {

    companion object {
        const val BASE_URL: String = "https://api.github.com/"
    }

    @Singleton
    @Provides
    fun provideRetrofitService(): ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create()
        ).build()
        .create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideDatabase(application: Application) = RepositoryDatabase.getDatabase(application)

    @Singleton
    @Provides
    fun provideDao(db: RepositoryDatabase) = db.repositoryDao()

    @Singleton
    @Provides
    fun provideRepository(
        repositoryDao: RepositoryDao,
        apiService: ApiService
    ) = MainRepository(repositoryDao, apiService)

    @Provides
    @Singleton
    fun connectionChecker() = ConnectionChecker()

}